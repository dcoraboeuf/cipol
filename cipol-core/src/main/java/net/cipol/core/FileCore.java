package net.cipol.core;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import net.cipol.api.FileService;
import net.cipol.api.HomeService;

@Service
public class FileCore implements FileService {
	
	private final Logger logger = LoggerFactory.getLogger(FileService.class);

	private final LoadingCache<String, ReadWriteLock> lockCache = CacheBuilder
			.newBuilder().build(new CacheLoader<String, ReadWriteLock>() {
				@Override
				public ReadWriteLock load(String name) throws Exception {
					return new ReentrantReadWriteLock();
				}
			});

	private final HomeService homeService;

	@Autowired
	public FileCore(HomeService homeService) {
		this.homeService = homeService;
	}

	@Override
	public <T> T read(Class<T> type, String id) {
		// Tries to lock
		Lock lock = getReadLock(type, id);
		try {
			return readSafe(type, id);
		} finally {
			lock.unlock();
		}
	}

	protected Lock getReadLock(Class<?> type, String id) {
		// Lock key
		String lockKey = getLockKey(type, id);
		try {
			// Gets the RW lock
			ReadWriteLock rwLock = lockCache.get(lockKey);
			// Acquires the read
			Lock readLock = rwLock.readLock();
			boolean locked = readLock.tryLock(1, TimeUnit.SECONDS);
			// Not locked?
			if (!locked) {
				throw new CannotAcquireLockException(lockKey);
			}
			// OK
			return readLock;
			// JDK7 Group exceptions
		} catch (ExecutionException ex) {
			throw new CannotAcquireLockException(ex, lockKey);
		} catch (InterruptedException ex) {
			throw new CannotAcquireLockException(ex, lockKey);
		}
	}

	protected String getLockKey(Class<?> type, String id) {
		return String.format("%s#%s", type.getName(), id);
	}

	protected <T> T readSafe(Class<T> type, String id) {
        File file = getFile(type, id);
        return load(type, file);
	}

	protected File getFile(Class<?> type, String id) {
		String fileName = getFileName (type, id);
		return homeService.getFile(fileName);
	}

	private String getFileName(Class<?> type, String id) {
		return String.format("%s.%s.json", type.getName(), id);
	}

    protected <T> T load(Class<T> type, File file) {
        logger.debug("[file] Loading {} from {}", type, file);
        if (file.exists()) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
            mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
            mapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
            try {
                return mapper.readValue(file, type);
            } catch (IOException ex) {
            	throw new CannotReadFileException (ex, file, type.getName());
            }
        } else {
        	throw new CannotFindFileException (file, type.getName());
        }
    }

}
