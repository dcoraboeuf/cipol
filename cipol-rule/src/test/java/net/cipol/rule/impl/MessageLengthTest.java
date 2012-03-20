package net.cipol.rule.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import net.cipol.model.CommitInformation;
import net.cipol.rule.RuleExecutionResult;
import net.cipol.rule.RuleExecutionResultType;

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

}
