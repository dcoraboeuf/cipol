package net.cipol.jira.impl;

import net.cipol.CipolProfiles;
import net.cipol.api.ConfigService;
import net.cipol.jira.JIRAConfig;
import net.cipol.jira.model.JIRAIssue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile({CipolProfiles.TEST, CipolProfiles.IT})
class JIRATestService extends JIRAServiceImpl {
	
	@Autowired
	JIRATestService(ConfigService configService) {
		super(configService);
	}

	@Override
	public JIRAIssue getIssue(JIRAConfig jiraConfig, final String key) {
		return configService.loadConfig(JIRAIssue.class, key);
	}

}