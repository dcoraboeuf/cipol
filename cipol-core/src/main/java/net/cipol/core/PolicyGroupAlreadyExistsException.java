package net.cipol.core;

import net.cipol.model.support.CoreException;

public class PolicyGroupAlreadyExistsException extends CoreException {

	public PolicyGroupAlreadyExistsException(String uid, String name) {
		super (uid, name);
	}

}
