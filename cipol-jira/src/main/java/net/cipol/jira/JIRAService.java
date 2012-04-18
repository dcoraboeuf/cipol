package net.cipol.jira;

import com.atlassian.jira.rest.client.domain.Issue;

public interface JIRAService {

	JIRAConfig loadJIRAConfig(String jiraConfigId);

	Issue getIssue(JIRAConfig jiraConfig, String key);

}
