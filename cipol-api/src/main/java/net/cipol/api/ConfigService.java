package net.cipol.api;

public interface ConfigService {
	
	<T> T loadConfig (Class<T> actualType, String reference);

	String loadParameter(Class<?> type, String reference, String name, boolean required, String defaultValue);

	String loadGeneralParameter(String name, boolean required, String defaultValue);

}
