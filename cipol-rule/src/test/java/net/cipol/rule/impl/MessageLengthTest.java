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

public class MessageLengthTest extends AbstractRuleTest {
	
	@Test
	public void too_short() {
		MessageLengthRule rule = new MessageLengthRule();
		CommitInformation commitInformation = new CommitInformation();
		commitInformation.setMessage("Too short");
		RuleExecutionResult result = rule.apply(context, new MessageLength(10), commitInformation);
		assertNotNull(result);
		assertEquals(RuleExecutionResultType.FAIL, result.getType());
		assertEquals("[MESSAGE-001] Message must be at least 10 characters long.", result.getMessage());
	}
	
	@Test
	public void ok() {
		MessageLengthRule rule = new MessageLengthRule();
		CommitInformation commitInformation = new CommitInformation();
		commitInformation.setMessage("Long enough");
		RuleExecutionResult result = rule.apply(context, new MessageLength(10), commitInformation);
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
		rule.createOptions(Collections.singletonMap("minlength", "1xx0"));
	}
	
	@Test
	public void options_ok() {
		MessageLengthRule rule = new MessageLengthRule();
		MessageLength options = rule.createOptions(Collections.singletonMap("minlength", "10"));
		assertNotNull(options);
		assertEquals(10, options.getMinlength());
	}

}
