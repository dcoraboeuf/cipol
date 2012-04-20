package net.cipol.jira.rule;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.cipol.jira.JIRAConfig;
import net.cipol.jira.JIRAService;
import net.cipol.jira.model.JIRAIssue;
import net.cipol.model.CommitInformation;
import net.cipol.rule.RuleExecutionContext;
import net.cipol.rule.RuleExecutionResult;
import net.cipol.rule.support.AbstractRule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JIRAIssueExistsRule extends AbstractRule<JIRAGenericRuleOptions> {
	
	public static final Pattern ISSUE_PATTERN = Pattern.compile("([A-Z][A-Z0-9_\\-]+)-(\\d+)");
	
	private final JIRAService jiraService;

	@Autowired
	public JIRAIssueExistsRule(JIRAService jiraService) {
		super("jira-issue-exists");
		this.jiraService = jiraService;
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
		// Gets the JIRA configuration
		JIRAConfig jiraConfig = jiraService.loadJIRAConfig(options.getJiraConfigId());
		// TODO Checks for existing issues in the context
		// Parses the message
		Matcher matcher = ISSUE_PATTERN.matcher(commitInformation.getMessage());
		// Finds all matches
		int issueCount = 0;
		List<JIRAIssue> issues = new ArrayList<JIRAIssue>();
		while (matcher.find()) {
			String project = matcher.group(1);
			String number = matcher.group(2);
			String key = String.format("%s-%s", project, number);
			// Controls if the project is a JIRA project
			if (!isProjectExcluded(context, options, project)) {
				// Controls only if the ticket is excluded or not
				if (isKeyExcluded()) {
					// The issue is taken into account, but not fetched from JIRA
					issueCount++;
				}
				// Gets the issue from JIRA
				else {
					JIRAIssue issue = jiraService.getIssue (jiraConfig, key);
					// Adds to the list
					if (issue != null) {
						issues.add(issue);
						issueCount++;
					}
				}
			}
		}
		// TODO Puts the issues in the context
		// Result
		if (issueCount > 0) {
			return ok();
		} else {
			return fail(context, "message");
		}
	}

	private boolean isKeyExcluded() {
		// TODO Auto-generated method stub
		return false;
	}

	private boolean isProjectExcluded(RuleExecutionContext context,
			JIRAGenericRuleOptions options, String project) {
		// TODO Auto-generated method stub
		return false;
	}

}
