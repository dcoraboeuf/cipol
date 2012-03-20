package net.cipol.core;

import net.cipol.model.support.CoreException;


public class CannotAcquireLockException extends CoreException {

	public CannotAcquireLockException(String key) {
		super(key);
	}

	public CannotAcquireLockException(Exception ex, String key) {
		super(ex, key);
	}

}
