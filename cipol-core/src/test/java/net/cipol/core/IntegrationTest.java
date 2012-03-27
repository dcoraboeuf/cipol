package net.cipol.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;

import net.cipol.api.APIService;
import net.cipol.api.HomeService;
import net.cipol.model.CommitInformation;
import net.cipol.model.ValidationDetail;
import net.cipol.model.ValidationReport;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:META-INF/spring/*.xml" })
@ActiveProfiles(profiles = { "test" })
public class IntegrationTest {

	@Autowired
	private APIService api;

	@Autowired
	private HomeService homeService;

	@Test
	public void check() {
		// Configuration OK
		assertNotNull(api);
		assertNotNull(homeService);
	}

	@Test
	public void home() {
		File file = homeService.getFile("net.cipol.model.Policy.bare.json");
		assertNotNull(file);
		assertTrue(file.exists());
	}
	
	@Test(expected = PolicyNotFoundException.class)
	public void validate_no_policy() {
		CommitInformation ci = new CommitInformation();
		api.validate("notfound", ci);
	}
	
	@Test
	public void validate_bare() {
		CommitInformation ci = new CommitInformation();
		ValidationReport report = api.validate("bare", ci);
		assertNotNull(report);
		assertTrue(report.isSuccess());
		assertNull(report.getMessage());
	}
	
	@Test(expected = RuleNotFoundException.class)
	public void validate_rule_not_found() {
		CommitInformation ci = new CommitInformation();
		ci.addPaths("/");
		api.validate("rulenotfound", ci);
	}
	
	@Test
	public void validate_on_message_nok() {
		CommitInformation ci = new CommitInformation();
		ci.setMessage("Too short");
		ci.addPaths("/README");
		ValidationReport report = api.validate("test", ci);
		assertNotNull(report);
		assertFalse(report.isSuccess());
		assertEquals("[MESSAGE-001] Message must be at least 10 characters long.", report.getMessage());
		List<ValidationDetail> details = report.getDetails();
		assertNotNull(details);
		assertEquals(1, details.size());
		{
			ValidationDetail detail = details.get(0);
			assertNotNull(detail);
			assertEquals("message", detail.getRuleId());
			assertEquals("The message must be at least 10 characters long.", detail.getRuleDescription());
			assertEquals("/**", detail.getPath());
			assertFalse(detail.isSuccess());
			assertEquals("[MESSAGE-001] Message must be at least 10 characters long.", detail.getMessage());
		}
	}
	
	@Test
	public void validate_on_message_ok() {
		CommitInformation ci = new CommitInformation();
		ci.setMessage("Long enough");
		ci.addPaths("README");
		ValidationReport report = api.validate("message", ci);
		assertNotNull(report);
		assertTrue(report.isSuccess());
		assertNull(report.getMessage());
		List<ValidationDetail> details = report.getDetails();
		assertNotNull(details);
		assertEquals(1, details.size());
		{
			ValidationDetail detail = details.get(0);
			assertNotNull(detail);
			assertEquals("message", detail.getRuleId());
			assertEquals("The message must be at least 10 characters long.", detail.getRuleDescription());
			assertEquals("", detail.getPath());
			assertTrue(detail.isSuccess());
			assertNull(detail.getMessage());
		}
	}
	
	@Test
	public void validate_test_authenticated_nok() {
		CommitInformation ci = new CommitInformation();
		ci.setMessage("Long enough");
		ci.setAuthor("");
		ci.addPaths("/doc/index.html");
		ValidationReport report = api.validate("test", ci);
		assertNotNull(report);
		assertFalse(report.isSuccess());
		assertEquals("[AUTHENTICATED-001] The authentication information is missing.", report.getMessage());
		List<ValidationDetail> details = report.getDetails();
		assertNotNull(details);
		assertEquals(2, details.size());
		{
			ValidationDetail detail = details.get(0);
			assertNotNull(detail);
			assertEquals("message", detail.getRuleId());
			assertEquals("The message must be at least 10 characters long.", detail.getRuleDescription());
			assertEquals("/**", detail.getPath());
			assertTrue(detail.isSuccess());
			assertNull(detail.getMessage());
		}
		{
			ValidationDetail detail = details.get(1);
			assertNotNull(detail);
			assertEquals("authenticated", detail.getRuleId());
			assertEquals("An author must be provided.", detail.getRuleDescription());
			assertEquals("/**", detail.getPath());
			assertFalse(detail.isSuccess());
			assertEquals("[AUTHENTICATED-001] The authentication information is missing.", detail.getMessage());
		}
	}
	
	@Test
	public void validate_test_authorization_nok() {
		CommitInformation ci = new CommitInformation();
		ci.setMessage("Long enough");
		ci.setAuthor("lambda");
		ci.addPaths("/README", "/pom.xml");
		ValidationReport report = api.validate("test", ci);
		assertNotNull(report);
		assertFalse(report.isSuccess());
		assertEquals("[AUTHORIZATION-001] Author \"lambda\" is not authorized.", report.getMessage());
	}
	
	@Test
	public void validate_test_authorization_ok_1() {
		CommitInformation ci = new CommitInformation();
		ci.setMessage("Long enough");
		ci.setAuthor("admin");
		ci.addPaths("/README", "/pom.xml");
		ValidationReport report = api.validate("test", ci);
		assertNotNull(report);
		assertTrue(report.isSuccess());
		assertNull(report.getMessage());
	}
	
	@Test
	public void validate_test_authorization_ok_2() {
		CommitInformation ci = new CommitInformation();
		ci.setMessage("Long enough");
		ci.setAuthor("translator1");
		ci.addPaths("/src/main/resources/META-INF/resources/core_fr.properties");
		ValidationReport report = api.validate("test", ci);
		assertNotNull(report);
		assertTrue(report.isSuccess());
		assertNull(report.getMessage());
	}

}
