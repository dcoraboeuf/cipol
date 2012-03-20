package net.cipol.rule.support;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import net.cipol.rule.RuleExecutionResult;
import net.cipol.rule.RuleExecutionResultType;

import org.junit.Test;

public class OKRuleExecutionResultTest {
	
	@Test
	public void instance() {
		RuleExecutionResult result = OKRuleExecutionResult.INSTANCE;
		assertNotNull(result);
		assertEquals(RuleExecutionResultType.OK, result.getType());
		assertNull(result.getMessage());
	}

}
