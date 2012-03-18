package net.cipol.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;

import net.cipol.api.FileService;
import net.cipol.api.PolicyService;
import net.cipol.model.Policy;

import org.junit.Before;
import org.junit.Test;

public class PolicyCoreTest {
	
	private PolicyService policyService;
	private FileService fileService;
	
	@Before
	public void before() {
		fileService = mock(FileService.class);
		// No file
		when(fileService.read(Policy.class, "0")).thenThrow(new CannotFindFileException(new File("."), ""));
		// No rule
		Policy policy = new Policy();
		policy.setUid("1");
		policy.setName("Policy 1");
		when(fileService.read(Policy.class, "1")).thenReturn(policy);
		// OK
		policyService = new PolicyCore(fileService);
	}
	
	@Test(expected = PolicyNotFoundException.class)
	public void load_not_found() {
		policyService.loadPolicy("0");
	}
	
	@Test
	public void load() {
		Policy policy = policyService.loadPolicy("1");
		assertNotNull(policy);
		assertEquals("1", policy.getUid());
		assertEquals("Policy 1", policy.getName());
	}

}
