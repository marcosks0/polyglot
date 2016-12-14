package polyglot.core;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Locale;
import java.util.ResourceBundle;

import polyglot.core.proxy.MessageProxyHandler;
import polyglot.core.proxy.ResourceBundleProxyHandler;

public final class Polyglot {
	
	private Polyglot() {
	}
	
	@SuppressWarnings("unchecked")
	private static <T> T toProxy(Class<T> proxyClass, InvocationHandler handler) {
		return (T) Proxy.newProxyInstance(proxyClass.getClassLoader(), new Class<?>[] { proxyClass }, handler);
	}
	
	public static <T> T of(Class<T> messageClass, Locale locale) {
		MessageProxyHandler handler = new MessageProxyHandler(messageClass, locale);
		return toProxy(messageClass, handler);
	}
	
	public static <T> T of(Class<T> messageClass, ResourceBundle rb) {
		ResourceBundleProxyHandler handler = new ResourceBundleProxyHandler(rb);
		return toProxy(messageClass, handler);
	}
	
	public static <T> T of(Class<T> messageClass, String bundleBaseName) {
		return of(messageClass, ResourceBundle.getBundle(bundleBaseName));
	}
	
	public static <T> T of(Class<T> messageClass, String bundleBaseName, Locale locale) {
		return of(messageClass, ResourceBundle.getBundle(bundleBaseName, locale));
	}
}
