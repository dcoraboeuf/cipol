package net.cipol.rule.support;

import java.util.Map;

import net.cipol.model.support.CoreException;

public class RuleParamNotFoundException extends CoreException {

	public RuleParamNotFoundException(String id, String name,
			Map<String, String> params) {
		super(id, name, params);
	}

}
