package polyglot.core.proxy;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.LocaleUtils;

import polyglot.core.Message;
import polyglot.core.Messages;
import polyglot.utils.TemplateBuilder;

public class MessageProxyHandler implements InvocationHandler {
	
	private Map<String, String> messages;
	private Class<?> messageClass;
	private Locale locale;
	
	public MessageProxyHandler(Class<?> messageClass, Locale locale) {
		Objects.requireNonNull(messageClass);
		Objects.requireNonNull(locale);
		
		this.messages = new HashMap<>();
		this.messageClass = messageClass;
		this.locale = locale;
		
		fillMessages();
	}
	
	private void fillMessages() {
		messages.clear();
		
		for (Method messageMethod : messageClass.getDeclaredMethods()) {
			Message message = getMessageForLocale(messageMethod);
			String messageTemplate = (Objects.nonNull(message) ? message.value() : messageMethod.getName());
			
			messages.put(messageMethod.getName(), messageTemplate);
		}
	}
	
	private Message getMessageForLocale(Method method) {
		if (method.isAnnotationPresent(Message.class)) {
			return method.getDeclaredAnnotation(Message.class);
		}

		if (method.isAnnotationPresent(Messages.class)) {
			for (Message simpeMessage : method.getDeclaredAnnotation(Messages.class).value()) {
				
				Locale messageLocale = LocaleUtils.toLocale(simpeMessage.locale());

				if (locale.equals(messageLocale)) {
					return simpeMessage;
				}
			}
		}
		
		return null;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

		Class<?> messageReturnType = method.getReturnType();
		String templateMessage = messages.get(method.getName());
		
		if (String.class.equals(messageReturnType)) {
			return MessageFormat.format(templateMessage, args);
		}
		
		if (TemplateBuilder.class.equals(messageReturnType)) {
			return new TemplateBuilder(templateMessage);
		}
		
		if (Throwable.class.isAssignableFrom(messageReturnType)) {
			Constructor<?> msgExceptionConstructor = messageReturnType.getDeclaredConstructor(String.class);
			return msgExceptionConstructor.newInstance(MessageFormat.format(templateMessage, args));
		}
		
		return null;
	}
	
}

