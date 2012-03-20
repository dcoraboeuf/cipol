package net.sf.cipol.rule.support;

import net.cipol.model.CommitInformation;
import net.sf.cipol.rule.RuleExecutionContext;
import net.sf.cipol.rule.RuleExecutionResult;

public abstract class AbstractPathIndependantRule<T> extends AbstractRule<T> {

	protected AbstractPathIndependantRule(String id) {
		super(id);
	}

	@Override
	public RuleExecutionResult apply(RuleExecutionContext context, T options,
			CommitInformation commitInformation) {
		String author = commitInformation.getAuthor();
		String message = commitInformation.getMessage();
		return apply (context, options, author, message);
	}

	protected abstract RuleExecutionResult apply(RuleExecutionContext context, T options, String author, String message);

}
