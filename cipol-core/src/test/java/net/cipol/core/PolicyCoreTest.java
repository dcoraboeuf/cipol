package net.cipol.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.sql.SQLException;
import java.util.List;

import net.cipol.api.PolicyService;
import net.cipol.model.Policy;
import net.cipol.test.AbstractIntegrationTest;

import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.ITable;
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
	public void create() {
		String uid = policyService.createPolicy("Created policy");
		assertNotNull(uid);
		Policy policy = policyService.loadPolicy(uid);
		assertEquals(uid, policy.getUid());
		assertEquals("Created policy", policy.getName());
		assertEquals("", policy.getDescription());
	}
	
	@Test
	public void delete() throws DataSetException, SQLException {
		// Initial situation for parameters
		List<Integer> paramIds = getInitialSituation(3, "id",
					"PARAMS", "select p.* from param p, ruleset rs, ruledef rd where rs.policy = '200' and rs.id = rd.ruleset and p.category = 'RULEDEF' and p.reference = rd.id");
		// Initial situation for groups
		List<Integer> groupIds = getInitialSituation(1, "id",
					"GROUPS", "select * from groups where category ='POLICY' and reference = '200'");
		// Actual deletion
		policyService.deletePolicy("200");
		// Now, we must check that everything has been deleted
		// Policy
		ITable policy = getTable("POLICY", "select * from POLICY where uid = '200'");
		assertEquals(0, policy.getRowCount());
		// Rulesets, we are confident because of the cascade delete
		ITable rulesets = getTable("RULEDEF",
				"select * from ruleset rs where rs.policy = '200'");
		assertEquals(0, rulesets.getRowCount());
		// Rules
		ITable rules = getTable("RULEDEF",
				"select * from ruleset rs, ruledef rd where rs.policy = '200' and rs.id = rd.ruleset");
		assertEquals(0, rules.getRowCount());
		// Params
		for (int paramId : paramIds) {
			assertEquals(
					String.format("Param id=%d was not deleted", paramId),
					0, getTable("PARAM_BY_ID_" + paramId, "select * from PARAM where id = %d", paramId).getRowCount());
		}
		// Groups
		for (int groupId : groupIds) {
			assertEquals(
					String.format("Group id=%d was not deleted", groupId),
					0, getTable("GROUP_BY_ID_" + groupId, "select * from GROUPS where id = %d", groupId).getRowCount());
		}
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
