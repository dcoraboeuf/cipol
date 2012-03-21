package net.cipol.rule.support;

import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.Map;

import org.junit.Test;

import net.cipol.model.CommitInformation;
import net.cipol.rule.RuleExecutionContext;
import net.cipol.rule.RuleExecutionResult;

public class AbstractRuleTest {
	
	private final AbstractRule<?> rule = new AbstractRule<Object>("test") {
		@Override
		public RuleExecutionResult apply(RuleExecutionContext context,
				Object options, CommitInformation commitInformation) {
			throw new UnsupportedOperationException();
		}
		@Override
		public Object createOptions(Map<String, String> parameters) {
			throw new UnsupportedOperationException();
		}
		@Override
		public String getDescription(RuleExecutionContext context,
				Object options) {
			throw new UnsupportedOperationException();
		}
	};
	
	@Test
	public void getId() {
		assertEquals("test", rule.getId());
	}
	
	@Test
	public void getInt_notrequired_notthere() {
		int value = rule.getInt(Collections.<String,String>emptyMap(), "name", false, 20);
		assertEquals(20, value);
	}
	
	@Test
	public void getInt_notrequired_ok() {
		int value = rule.getInt(Collections.singletonMap("name", "10"), "name", false, 20);
		assertEquals(10, value);
	}
	
	@Test(expected = RuleParamNumberFormatException.class)
	public void getInt_notrequired_wrongformat() {
		rule.getInt(Collections.singletonMap("name", "1xx0"), "name", false, 20);
	}
	
	@Test(expected = RuleParamNotFoundException.class)
	public void getInt_required_notthere() {
		rule.getInt(Collections.<String,String>emptyMap(), "name", true, 20);
	}
	
	@Test
	public void getInt_required_ok() {
		int value = rule.getInt(Collections.singletonMap("name", "10"), "name", true, 20);
		assertEquals(10, value);
	}

}
