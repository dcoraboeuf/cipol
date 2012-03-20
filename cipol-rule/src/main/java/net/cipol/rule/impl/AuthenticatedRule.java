package net.cipol.rule.impl;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import net.cipol.model.CommitInformation;
import net.cipol.rule.RuleExecutionContext;
import net.cipol.rule.RuleExecutionResult;
import net.cipol.rule.support.AbstractRule;

@Component
public class AuthenticatedRule extends AbstractRule<Void> {
	
	public static final String RULE_ID = "authenticated";

	public AuthenticatedRule() {
		super(RULE_ID);
	}

	@Override
	public Void createOptions(Map<String, String> parameters) {
		return null;
	}

	@Override
	public String getDescription(RuleExecutionContext context, Void options) {
		return getMessage(context, "description");
	}

	@Override
	public RuleExecutionResult apply(RuleExecutionContext context,
			Void options, CommitInformation commitInformation) {
		String author = commitInformation.getAuthor();
		if (StringUtils.isNotBlank(author)) {
			return ok();
		} else {
			return fail(context, "message");
		}
	}

}
