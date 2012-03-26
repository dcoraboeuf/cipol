package net.cipol.jira;

public interface JIRAService {

	JIRAConfig loadJIRAConfig(String jiraConfigId);

	JIRAIssue getIssue(JIRAConfig jiraConfig, String key);

}
