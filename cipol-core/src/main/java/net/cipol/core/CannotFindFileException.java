package net.cipol.core;

import java.io.File;


public class CannotFindFileException extends CoreException {

	public CannotFindFileException(File file, String name) {
		super(file, name);
	}

}
