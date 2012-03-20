package net.cipol.core;

import java.io.File;

import net.cipol.model.support.CoreException;


public class CannotFindFileException extends CoreException {

	public CannotFindFileException(File file, String name) {
		super(file, name);
	}

}
