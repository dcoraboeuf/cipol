package net.cipol.rule.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Collections;

import net.cipol.model.CommitInformation;
import net.cipol.rule.RuleExecutionResult;
import net.cipol.rule.RuleExecutionResultType;

import org.junit.Test;

public class AuthenticatedRuleTest extends AbstractRuleTest {
	
	@Test
	public void nok() {
		AuthenticatedRule rule = new AuthenticatedRule();
		CommitInformation commitInformation = new CommitInformation();
		commitInformation.setAuthor("");
		RuleExecutionResult result = rule.apply(context, null, commitInformation);
		assertNotNull(result);
		assertEquals(RuleExecutionResultType.FAIL, result.getType());
		assertEquals("[AUTHENTICATED-001] The authentication information is missing.", result.getMessage());
	}
	
	@Test
	public void ok() {
		AuthenticatedRule rule = new AuthenticatedRule();
		CommitInformation commitInformation = new CommitInformation();
		commitInformation.setAuthor("any");
		RuleExecutionResult result = rule.apply(context, null, commitInformation);
		assertNotNull(result);
		assertEquals(RuleExecutionResultType.OK, result.getType());
		assertNull(result.getMessage());
	}
	
	@Test
	public void description() {
		AuthenticatedRule rule = new AuthenticatedRule();
		String description = rule.getDescription(context, null);
		assertEquals("An author must be provided.", description);
	}
	
	@Test
	public void options() {
		AuthenticatedRule rule = new AuthenticatedRule();
		Void options = rule.createOptions(Collections.<String,String>emptyMap());
		assertNull(options);
	}

}
