package net.cipol.core;

import net.cipol.model.support.CoreException;

public class PolicyNotFoundException extends CoreException {

	public PolicyNotFoundException(String policyId) {
		super(policyId);
	}

}
