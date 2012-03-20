package net.cipol.rule.support;

import net.cipol.rule.RuleExecutionResult;
import net.cipol.rule.RuleExecutionResultType;

public final class OKRuleExecutionResult implements RuleExecutionResult {

	public static final RuleExecutionResult INSTANCE = new OKRuleExecutionResult();
	
	private OKRuleExecutionResult() {
	}

	@Override
	public RuleExecutionResultType getType() {
		return RuleExecutionResultType.OK;
	}

	@Override
	public String getMessage() {
		return null;
	}

}
