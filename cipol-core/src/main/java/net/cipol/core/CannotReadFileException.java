package net.cipol.core;

import java.io.File;
import java.io.IOException;

import net.cipol.model.support.CoreException;


public class CannotReadFileException extends CoreException {

	public CannotReadFileException(IOException ex, File file, String name) {
		super(ex, file, name, ex);
	}

}
