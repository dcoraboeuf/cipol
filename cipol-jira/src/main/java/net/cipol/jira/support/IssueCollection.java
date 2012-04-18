package net.cipol.jira.support;

import java.util.List;

import com.atlassian.jira.rest.client.domain.Issue;

public class IssueCollection {

	private List<Issue> issues;

	public List<Issue> getIssues() {
		return issues;
	}

	public void setIssues(List<Issue> issues) {
		this.issues = issues;
	}

}
