package net.cipol.rule.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import net.cipol.model.CommitInformation;
import net.cipol.rule.RuleExecutionResult;
import net.cipol.rule.RuleExecutionResultType;

import org.junit.Test;

public class AuthorizationRuleTest extends AbstractRuleTest {
	
	@Test
	public void no_list() {
		AuthorizationRule rule = new AuthorizationRule();
		CommitInformation commitInformation = new CommitInformation();
		commitInformation.setAuthor("test");
		RuleExecutionResult result = rule.apply(context, new AuthorizationRuleOptions("", ""), commitInformation);
		assertNotNull(result);
		assertEquals(RuleExecutionResultType.FAIL, result.getType());
		assertEquals("[AUTHORIZATION-001] Author \"test\" is not authorized.", result.getMessage());
	}
	
	@Test
	public void allowed_list_only_nok() {
		AuthorizationRule rule = new AuthorizationRule();
		CommitInformation commitInformation = new CommitInformation();
		commitInformation.setAuthor("test");
		RuleExecutionResult result = rule.apply(context, new AuthorizationRuleOptions("stephan,ursula", ""), commitInformation);
		assertNotNull(result);
		assertEquals(RuleExecutionResultType.FAIL, result.getType());
		assertEquals("[AUTHORIZATION-001] Author \"test\" is not authorized.", result.getMessage());
	}
	
	@Test
	public void allowed_list_only_ok() {
		AuthorizationRule rule = new AuthorizationRule();
		CommitInformation commitInformation = new CommitInformation();
		commitInformation.setAuthor("test");
		RuleExecutionResult result = rule.apply(context, new AuthorizationRuleOptions("stephan,test,ursula", ""), commitInformation);
		assertNotNull(result);
		assertEquals(RuleExecutionResultType.OK, result.getType());
		assertNull(result.getMessage());
	}
	
	@Test
	public void disallowed_list_only_nok() {
		AuthorizationRule rule = new AuthorizationRule();
		CommitInformation commitInformation = new CommitInformation();
		commitInformation.setAuthor("test");
		RuleExecutionResult result = rule.apply(context, new AuthorizationRuleOptions("", "stephan,ursula"), commitInformation);
		assertNotNull(result);
		assertEquals(RuleExecutionResultType.FAIL, result.getType());
		assertEquals("[AUTHORIZATION-001] Author \"test\" is not authorized.", result.getMessage());
	}
	
	@Test
	public void combined_list_nok() {
		AuthorizationRule rule = new AuthorizationRule();
		CommitInformation commitInformation = new CommitInformation();
		commitInformation.setAuthor("test");
		RuleExecutionResult result = rule.apply(context, new AuthorizationRuleOptions("test", "stephan,test,ursula"), commitInformation);
		assertNotNull(result);
		assertEquals(RuleExecutionResultType.FAIL, result.getType());
		assertEquals("[AUTHORIZATION-001] Author \"test\" is not authorized.", result.getMessage());
	}
	
	@Test
	public void combined_list_ok() {
		AuthorizationRule rule = new AuthorizationRule();
		CommitInformation commitInformation = new CommitInformation();
		commitInformation.setAuthor("test");
		RuleExecutionResult result = rule.apply(context, new AuthorizationRuleOptions("test", "stephan,ursula"), commitInformation);
		assertNotNull(result);
		assertEquals(RuleExecutionResultType.OK, result.getType());
		assertNull(result.getMessage());
	}
	
	@Test
	public void description() {
		AuthorizationRule rule = new AuthorizationRule();
		String description = rule.getDescription(context, new AuthorizationRuleOptions("a,b,c", "d,e"));
		assertEquals("Authorized to a,b,c and not authorized to d,e", description);
	}

}
