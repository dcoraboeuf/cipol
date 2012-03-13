package net.cipol.core;

import java.io.File;
import java.io.IOException;


public class CannotReadFileException extends CoreException {

	public CannotReadFileException(IOException ex, File file, String name) {
		super(ex, file, name, ex);
	}

}
