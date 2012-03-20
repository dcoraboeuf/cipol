package net.cipol.rule.support;

import org.apache.commons.lang.Validate;

import net.cipol.rule.RuleExecutionResult;
import net.cipol.rule.RuleExecutionResultType;

public class FailRuleExecutionResult implements RuleExecutionResult {
	
	private final String message;
	
	public FailRuleExecutionResult(String message) {
		Validate.notEmpty(message);
		this.message = message;
	}

	@Override
	public RuleExecutionResultType getType() {
		return RuleExecutionResultType.FAIL;
	}
	
	@Override
	public String getMessage() {
		return message;
	}

}
