package net.cipol.core;

import net.cipol.model.support.CoreException;

public class ParameterRequiredException extends CoreException {

	public ParameterRequiredException(String category, String reference,
			String name) {
		super(category, reference, name);
	}

}
