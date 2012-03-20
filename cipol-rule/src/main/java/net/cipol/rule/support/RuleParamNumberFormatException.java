package net.cipol.rule.support;

import net.cipol.model.support.CoreException;

public class RuleParamNumberFormatException extends CoreException {

	public RuleParamNumberFormatException(NumberFormatException ex, String id,
			String name, String value) {
		super(ex, id, name, value, ex);
	}

}
