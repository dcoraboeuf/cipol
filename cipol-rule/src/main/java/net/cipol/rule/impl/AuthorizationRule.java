package net.cipol.rule.impl;

import java.util.Map;

import org.springframework.stereotype.Component;

import net.cipol.model.CommitInformation;
import net.cipol.rule.RuleExecutionContext;
import net.cipol.rule.RuleExecutionResult;
import net.cipol.rule.support.AbstractRule;

@Component
public class AuthorizationRule extends AbstractRule<AuthorizationRuleOptions> {
	
	public static final String RULE_ID = "authorization";

	public AuthorizationRule() {
		super(RULE_ID);
	}

	@Override
	public AuthorizationRuleOptions createOptions(Map<String, String> parameters) {
		return new AuthorizationRuleOptions(
				getString(parameters, "allowed", false, ""),
				getString(parameters, "disallowed", false, "")
			);
	}

	@Override
	public String getDescription(RuleExecutionContext context,
			AuthorizationRuleOptions options) {
		return getMessage(context, "description", options.getAllowedString(), options.getDisallowedString());
	}

	@Override
	public RuleExecutionResult apply(RuleExecutionContext context,
			AuthorizationRuleOptions options,
			CommitInformation commitInformation) {
		String author = commitInformation.getAuthor();
		if (options.isAllowed(author)) {
			return ok();
		} else {
			return fail(context, "message", author);
		}
	}

}
