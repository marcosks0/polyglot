package polyglot.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class TemplateBuilder {

	private String template;
	private Map<String, Object> parameters;
	
	public TemplateBuilder(String template) {
		this.template = template;
		this.parameters = new HashMap<>();
	}
	
	public TemplateBuilder withParameter(String name, Object value) {
		parameters.put(name, value);
		return this;
	}
	
	@Override
	public String toString() {
	    Pattern pattern = Pattern.compile("\\{(.+?)\\}");
	    Matcher matcher = pattern.matcher(template);

	    StringBuffer buffer = new StringBuffer();
	    while (matcher.find()) {
	        if (parameters.containsKey(matcher.group(1))) {
	            Object replacement = parameters.get(matcher.group(1));

	            matcher.appendReplacement(buffer, replacement != null ? Matcher.quoteReplacement(String.valueOf(replacement)) : "null");
	        }
	    }
	    matcher.appendTail(buffer);
	    
	    return buffer.toString();
	}
	
	public <T extends Throwable> T ofException(Class<T> throwableType) {
		try {
			Constructor<T> msgExceptionConstructor = throwableType.getDeclaredConstructor(String.class);
			return msgExceptionConstructor.newInstance(toString());
		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}
	
	public <T extends Throwable> void fire(Class<T> throwableType) throws T {
		throw ofException(throwableType);
	}
	
}
