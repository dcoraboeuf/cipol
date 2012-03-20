package net.sf.cipol.rule.support;

import net.sf.cipol.rule.RuleExecutionResult;
import net.sf.cipol.rule.RuleExecutionResultType;

public class FailRuleExecutionResult implements RuleExecutionResult {
	
	private final String message;
	
	public FailRuleExecutionResult(String message) {
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
