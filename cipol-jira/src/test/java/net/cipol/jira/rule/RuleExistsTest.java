package net.cipol.jira.rule;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import net.cipol.api.APIService;
import net.cipol.model.CommitInformation;
import net.cipol.model.ValidationReport;
import net.cipol.test.AbstractIntegrationTest;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class RuleExistsTest extends AbstractIntegrationTest {

	@Autowired
	private APIService api;
	
	@Test
	public void jira_issue_exists_nok() {
		CommitInformation ci = new CommitInformation();
		ci.setMessage("Long enough");
		ci.setAuthor("myself");
		ci.addPaths("some/file");
		ValidationReport report = api.validate("rule-exists", ci);
		assertNotNull(report);
		assertFalse(report.isSuccess());
		assertEquals("[JIRA-001] No existing JIRA issue was found in the message", report.getMessage());
	}
	
	@Test
	public void jira_issue_exists_ok() {
		CommitInformation ci = new CommitInformation();
		ci.setMessage("PROJ-455 Long enough");
		ci.setAuthor("myself");
		ci.addPaths("some/file");
		ValidationReport report = api.validate("rule-exists", ci);
		assertNotNull(report);
		assertTrue(report.isSuccess());
		assertNull(report.getMessage());
	}
	
}
