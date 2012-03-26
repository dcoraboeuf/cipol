package net.cipol.jira.rule;

import java.util.Map;

import org.springframework.stereotype.Component;

import net.cipol.model.CommitInformation;
import net.cipol.rule.RuleExecutionContext;
import net.cipol.rule.RuleExecutionResult;
import net.cipol.rule.support.AbstractRule;

@Component
public class JIRAIssueExistsRule extends AbstractRule<JIRAGenericRuleOptions> {

	protected JIRAIssueExistsRule() {
		super("jira-issue-exists");
	}

	@Override
	public JIRAGenericRuleOptions createOptions(Map<String, String> parameters) {
		return new JIRAGenericRuleOptions(getString(parameters, "jira-config-id", true, null));
	}

	@Override
	public String getDescription(RuleExecutionContext context,
			JIRAGenericRuleOptions options) {
		return getMessage(context, "description");
	}

	@Override
	public RuleExecutionResult apply(RuleExecutionContext context,
			JIRAGenericRuleOptions options, CommitInformation commitInformation) {
		// TODO Auto-generated method stub
		return ok();
	}

}
