package net.cipol.jira.rule;

public class JIRAGenericRuleOptions {

	private final String jiraConfigId;

	public JIRAGenericRuleOptions(String jiraConfigId) {
		this.jiraConfigId = jiraConfigId;
	}

	public String getJiraConfigId() {
		return jiraConfigId;
	}

}
