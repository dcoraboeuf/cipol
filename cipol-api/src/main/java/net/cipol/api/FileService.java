package net.cipol.api;

import java.util.List;

public interface FileService {

	<T> T read(Class<T> type, String id);

	<T> List<T> readAll(Class<T> type);

}
