package net.cipol.api;


public interface FileService {

	<T> T read(Class<T> type, String id);

}
