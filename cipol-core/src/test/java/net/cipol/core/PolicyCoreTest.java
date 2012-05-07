package net.cipol.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import net.cipol.api.PolicyService;
import net.cipol.model.Policy;
import net.cipol.test.AbstractIntegrationTest;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public class PolicyCoreTest extends AbstractIntegrationTest {
	
	@Autowired
	private PolicyService policyService;
	
	@Test(expected = PolicyNotFoundException.class)
	public void load_not_found() {
		policyService.loadPolicy("0");
	}
	
	@Test
	public void create() {
		String uid = policyService.createPolicy("Created policy");
		assertNotNull(uid);
		Policy policy = policyService.loadPolicy(uid);
		assertEquals(uid, policy.getUid());
		assertEquals("Created policy", policy.getName());
		assertEquals("", policy.getDescription());
	}
	
	@Test
	public void load() {
		Policy policy = policyService.loadPolicy("100");
		assertNotNull(policy);
		assertEquals("100", policy.getUid());
		assertEquals("Test", policy.getName());
		assertEquals("Test policy", policy.getDescription());
	}

}
