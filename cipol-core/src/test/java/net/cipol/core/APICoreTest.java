package net.cipol.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import net.cipol.api.APIService;
import net.cipol.api.PolicyService;
import net.cipol.api.model.VersionInformation;

import org.junit.Before;
import org.junit.Test;

public class APICoreTest {

	private APIService api;
	
	@Before
	public void before() {
		// Policy service
		PolicyService policyService = mock(PolicyService.class);
		// API
		api = new APICore("TEST", policyService);
	}

	@Test
	public void version() {
		VersionInformation version = api.getVersionInformation();
		assertNotNull(version);
		assertEquals("TEST", version.getVersionNumber());
	}

}
