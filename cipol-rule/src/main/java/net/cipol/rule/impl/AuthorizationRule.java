package net.cipol.rule.impl;

import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

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
		if (isAllowed(context, options, author)) {
			return ok();
		} else {
			return fail(context, "message", author);
		}
	}

	protected boolean isAllowed(RuleExecutionContext context,
			AuthorizationRuleOptions options, String author) {
		return belongsTo (context, author, options.getAllowed())
				&& !belongsTo (context, author, options.getDisallowed());
	}

	protected boolean belongsTo(final RuleExecutionContext context, final String author,
			Set<String> authorizations) {
		return Iterables.any(authorizations, new Predicate<String>(){
			@Override
			public boolean apply(String authorization) {
				if (StringUtils.startsWith(authorization, "@")) {
					String group = StringUtils.substring(authorization, 1);
					return context.isMemberOfGroup(author, group);
				} else {
					return StringUtils.equals(authorization, author);
				}
			}
		});
	}

}
