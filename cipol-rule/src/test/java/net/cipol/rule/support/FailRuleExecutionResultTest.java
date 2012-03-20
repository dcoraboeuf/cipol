package net.cipol.rule.support;

import static org.junit.Assert.assertEquals;
import net.cipol.rule.RuleExecutionResultType;

import org.junit.Test;

public class FailRuleExecutionResultTest {
	
	@Test(expected = IllegalArgumentException.class)
	public void message_null() {
		new FailRuleExecutionResult(null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void message_empty() {
		new FailRuleExecutionResult("");
	}
	
	@Test
	public void message_ok() {
		FailRuleExecutionResult result = new FailRuleExecutionResult("Failed");
		assertEquals(RuleExecutionResultType.FAIL, result.getType());
		assertEquals("Failed", result.getMessage());
	}

}
