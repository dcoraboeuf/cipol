package net.cipol.rule.impl;

import java.util.Locale;

import net.cipol.rule.RuleExecutionContext;
import net.sf.jstring.Strings;

import org.junit.Before;

public abstract class AbstractRuleTest {
	
	protected RuleExecutionContext context;

	@Before
	public void context() {
		final Strings strings = new Strings("META-INF.resources.rule-provided");
		context = new RuleExecutionContext() {
			
			@Override
			public String getMessage(String key, Object... parameters) {
				return strings.get(Locale.ENGLISH, key, parameters);
			}
		};
	}

}
