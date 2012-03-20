package net.sf.cipol.rule.support;

import net.sf.cipol.rule.RuleExecutionContext;
import net.sf.cipol.rule.RuleExecutionResult;

public abstract class AbstractMessageRule<T> extends AbstractPathIndependantRule<T> {

	protected AbstractMessageRule(String id) {
		super(id);
	}
	
	protected RuleExecutionResult apply(RuleExecutionContext context, T options, String author, String message) {
		return apply (context, options, message);
	}
	
	protected abstract RuleExecutionResult apply(RuleExecutionContext context, T options, String message);

}
