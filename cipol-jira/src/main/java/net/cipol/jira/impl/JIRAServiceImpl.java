package net.cipol.jira.impl;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import net.cipol.CipolProfiles;
import net.cipol.api.FileService;
import net.cipol.jira.JIRAConfig;
import net.cipol.jira.JIRAConnectionException;
import net.cipol.jira.JIRAService;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.atlassian.jira.rest.client.JiraRestClient;
import com.atlassian.jira.rest.client.NullProgressMonitor;
import com.atlassian.jira.rest.client.domain.Issue;
import com.atlassian.jira.rest.client.internal.jersey.JerseyJiraRestClientFactory;

@Service
@Profile({CipolProfiles.DEV, CipolProfiles.PROD})
public class JIRAServiceImpl implements JIRAService {
	
	protected final FileService fileService;
	
	@Autowired
	public JIRAServiceImpl(FileService fileService) {
		this.fileService = fileService;
	}

	@Override
	public JIRAConfig loadJIRAConfig(String jiraConfigId) {
		return fileService.read (JIRAConfig.class, jiraConfigId);
	}

	@Override
	public Issue getIssue(JIRAConfig jiraConfig, String key) {
		JiraRestClient client = getClient(jiraConfig);
		// Gets the issue
		Issue issue = client.getIssueClient().getIssue(key, new NullProgressMonitor());
		// OK
		return issue;
	}

	protected JiraRestClient getClient(JIRAConfig jiraConfig) {
		// TODO Uses a transactional JIRA session for performance reasons
		try {
			// Gets a factory
			JerseyJiraRestClientFactory factory = new JerseyJiraRestClientFactory();
			// Gets the client with basic authentication
			JiraRestClient client = factory.createWithBasicHttpAuthentication(new URI(jiraConfig.getUrl()), jiraConfig.getUser(), jiraConfig.getPassword());
			// OK
			return client;
		} catch (URISyntaxException ex) {
			throw new JIRAConnectionException(ex, jiraConfig);
		}
	}
	
	/**
	 * Test program 
	 */
	public static void main (String[] args) throws JsonGenerationException, JsonMappingException, IOException {
		// Arguments
		int index = 0;
		String url = args[index++];
		String user = args[index++];
		String password = args[index++];
		String key = args[index++];
		// Configuration
		JIRAConfig config = new JIRAConfig();
		config.setUrl(url);
		config.setUser(user);
		config.setPassword(password);
		// Service
		JIRAService service = new JIRAServiceImpl(null);
		// Call
		Issue issue = service.getIssue(config, key);
		// Prints
		ObjectMapper mapper = new ObjectMapper();
		System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(issue));
	}

}
