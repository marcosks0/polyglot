package polyglot.core.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.Objects;
import java.util.ResourceBundle;

import polyglot.core.BundleProperty;

public class ResourceBundleProxyHandler implements InvocationHandler {

	private ResourceBundle rb;
	
	public ResourceBundleProxyHandler(ResourceBundle rb) {
		Objects.requireNonNull(rb);
		this.rb = rb;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		String propertyName = method.getName();
		
		if (method.isAnnotationPresent(BundleProperty.class)) {
			propertyName = method.getDeclaredAnnotation(BundleProperty.class).value();
		}
		
		try {
			return MessageFormat.format(rb.getString(propertyName), args);
		} catch (MissingResourceException e) {
			return propertyName;
		}
	}

}
