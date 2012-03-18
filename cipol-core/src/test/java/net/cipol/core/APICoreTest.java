package net.cipol.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;

import net.cipol.api.APIService;
import net.cipol.api.PolicyService;
import net.cipol.model.CommitInformation;
import net.cipol.model.Policy;
import net.cipol.model.ValidationResult;
import net.cipol.model.VersionInformation;

import org.junit.Before;
import org.junit.Test;

public class APICoreTest {

	private APIService api;
	private PolicyService policyService;

	@Before
	public void before() {
		// Policy service
		policyService = mock(PolicyService.class);
		// API
		api = new APICore("TEST", policyService);
	}

	@Test
	public void version() {
		VersionInformation version = api.getVersionInformation();
		assertNotNull(version);
		assertEquals("TEST", version.getVersionNumber());
	}

	@Test(expected = CannotFindFileException.class)
	public void validate_no_policy() {
		when(policyService.loadPolicy("0")).thenThrow(
				new CannotFindFileException(new File("."), ""));
		api.validate("0", new CommitInformation());
	}

	@Test
	public void validate_no_rule() {
		Policy policy = new Policy();
		policy.setUid("1");
		policy.setName("Policy 1");
		when(policyService.loadPolicy("1")).thenReturn(policy);
		ValidationResult result = api.validate("1", new CommitInformation());
		assertNotNull(result);
		assertTrue(result.isValid());
		assertNotNull(result.getMessages());
		assertTrue(result.getMessages().isEmpty());
	}

}
