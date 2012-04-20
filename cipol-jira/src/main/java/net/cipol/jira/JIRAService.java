package net.cipol.jira;

import net.cipol.jira.model.JIRAIssue;

public interface JIRAService {

	JIRAConfig loadJIRAConfig(String jiraConfigId);

	JIRAIssue getIssue(JIRAConfig jiraConfig, String key);

}
