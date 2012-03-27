package net.cipol.jira.impl

import net.cipol.CipolProfiles;
import net.cipol.api.FileService;
import net.cipol.jira.JIRAConfig;
import net.cipol.jira.JIRAIssue;
import net.cipol.jira.support.JIRAIssueCollection;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

@Service
@Profile([CipolProfiles.TEST, CipolProfiles.IT])
class JIRATestService extends JIRAServiceImpl {
	
	@Autowired
	JIRATestService(FileService fileService) {
		super(fileService);
	}
	
	@Override
	public JIRAIssue getIssue(JIRAConfig jiraConfig, final String key) {
		// Loads issues for this config
		JIRAIssueCollection issues = fileService.read(JIRAIssueCollection.class, jiraConfig.getId());
		// OK
		return Iterables.find(issues.getIssues(), new Predicate<JIRAIssue>(){
			@Override
			public boolean apply(JIRAIssue issue) {
				return StringUtils.equals(key, issue.getKey());
			}
		}, null);
	}

}
