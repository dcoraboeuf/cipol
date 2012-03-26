package net.cipol.jira.impl;

import net.cipol.api.FileService;
import net.cipol.jira.JIRAConfig;
import net.cipol.jira.JIRAIssue;
import net.cipol.jira.JIRAService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("prod")
public class JIRAServiceImpl implements JIRAService {

	protected final FileService fileService;

	@Autowired
	public JIRAServiceImpl(FileService fileService) {
		this.fileService = fileService;
	}

	@Override
	@Cacheable("jira-config")
	public JIRAConfig loadJIRAConfig(String jiraConfigId) {
		return fileService.read(JIRAConfig.class, jiraConfigId);
	}
	
	@Override
	public JIRAIssue getIssue(JIRAConfig jiraConfig, String key) {
		// TODO Auto-generated method stub
		return null;
	}

}
