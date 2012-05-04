package net.cipol.api;

public interface ConfigService {
	
	<T> T loadConfig (Class<T> actualType, String reference);

}
