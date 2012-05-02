package net.cipol.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import net.cipol.api.PolicyService;
import net.cipol.api.RuleService;
import net.cipol.model.CommitInformation;
import net.cipol.model.Policy;
import net.cipol.model.ValidationReport;
import net.cipol.model.VersionInformation;
import net.sf.jstring.Strings;

import org.junit.Before;
import org.junit.Test;

public class APICoreTest {

	private APICore api;
	private PolicyService policyService;
	private RuleService ruleService;

	@Before
	public void before() {
		// Policy service
		policyService = mock(PolicyService.class);
		// Rule service
		ruleService = mock(RuleService.class);
		// Strings
		Strings strings = new Strings();
		// API
		api = new APICore("TEST", policyService, ruleService, strings);
	}

	@Test
	public void version() {
		VersionInformation version = api.getVersionInformation();
		assertNotNull(version);
		assertEquals("TEST", version.getVersionNumber());
	}

	@Test(expected = PolicyNotFoundException.class)
	public void validate_no_policy() {
		when(policyService.loadPolicy("0")).thenThrow(
				new PolicyNotFoundException("0"));
		api.validate("0", new CommitInformation());
	}

	@Test
	public void validate_no_rule() {
		Policy policy = new Policy();
		policy.setUid("1");
		policy.setName("Policy 1");
		when(policyService.loadPolicy("1")).thenReturn(policy);
		ValidationReport report = api.validate("1", new CommitInformation());
		assertNotNull(report);
		assertTrue(report.isSuccess());
		assertNull(report.getMessage());
	}

}
