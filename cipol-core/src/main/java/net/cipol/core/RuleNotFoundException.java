package net.cipol.core;

import net.cipol.model.support.CoreException;

public class RuleNotFoundException extends CoreException {

	public RuleNotFoundException(String ruleId) {
		super(ruleId);
	}

}
