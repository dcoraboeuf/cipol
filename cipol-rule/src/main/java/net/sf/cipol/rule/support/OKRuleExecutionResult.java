package net.sf.cipol.rule.support;

import net.sf.cipol.rule.RuleExecutionResult;
import net.sf.cipol.rule.RuleExecutionResultType;

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
