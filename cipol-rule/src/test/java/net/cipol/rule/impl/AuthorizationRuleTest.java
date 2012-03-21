package net.cipol.rule.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Collections;

import net.cipol.model.CommitInformation;
import net.cipol.rule.RuleExecutionResult;
import net.cipol.rule.RuleExecutionResultType;
import net.cipol.rule.support.RuleParamNotFoundException;
import net.cipol.rule.support.RuleParamNumberFormatException;

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
		MessageLengthRule rule = new MessageLengthRule();
		String description = rule.getDescription(context, new MessageLength(10));
		assertEquals("The message must be at least 10 characters long.", description);
	}
	
	@Test(expected = RuleParamNotFoundException.class)
	public void options_missing_param() {
		MessageLengthRule rule = new MessageLengthRule();
		rule.createOptions(Collections.<String,String>emptyMap());
	}
	
	@Test(expected = RuleParamNumberFormatException.class)
	public void options_wrong_format() {
		MessageLengthRule rule = new MessageLengthRule();
		rule.createOptions(Collections.singletonMap("maxlength", "1xx0"));
	}
	
	@Test
	public void options_ok() {
		MessageLengthRule rule = new MessageLengthRule();
		MessageLength options = rule.createOptions(Collections.singletonMap("maxlength", "10"));
		assertNotNull(options);
		assertEquals(10, options.getMaxlength());
	}

}
