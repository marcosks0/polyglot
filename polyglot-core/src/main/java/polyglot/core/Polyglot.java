package polyglot.core;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.LocaleUtils;

public final class Polyglot {
	
	private Polyglot() {
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T of(Class<T> messageClass, Locale locale) {
		MessageProxyHandler handler = new MessageProxyHandler(messageClass, locale);
		return (T) Proxy.newProxyInstance(messageClass.getClassLoader(), new Class<?>[] { messageClass }, handler);
	}
	
	private static class MessageProxyHandler implements InvocationHandler {
		
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
				
				if (Objects.isNull(message)) {
					continue;
				}
				
				messages.put(messageMethod.getName(), message.value());
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
	
}
