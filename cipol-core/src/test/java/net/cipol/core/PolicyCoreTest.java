package net.cipol.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import net.cipol.api.PolicyService;
import net.cipol.model.Policy;
import net.cipol.test.AbstractIntegrationTest;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class PolicyCoreTest extends AbstractIntegrationTest {
	
	@Autowired
	private PolicyService policyService;
	
	@Test(expected = PolicyNotFoundException.class)
	public void load_not_found() {
		policyService.loadPolicy("0");
	}
	
	@Test
	public void load() {
		Policy policy = policyService.loadPolicy("1");
		assertNotNull(policy);
		assertEquals("1", policy.getUid());
		assertEquals("Test", policy.getName());
		assertEquals("Test policy", policy.getDescription());
	}

}
