package net.cipol.rule.support;

import java.util.Map;

import net.cipol.rule.Rule;
import net.cipol.rule.RuleExecutionContext;
import net.cipol.rule.RuleExecutionResult;

import org.apache.commons.lang.StringUtils;

public abstract class AbstractRule<T> implements Rule<T> {

	private final String id;

	protected AbstractRule(String id) {
		this.id = id;
	}

	@Override
	public String getId() {
		return id;
	}
	
	protected int getInt (Map<String, String> params, String name, boolean required, int defaultValue) {
		String value = getString (params, name, required, null);
		if (value == null) {
			return defaultValue;
		} else {
			try {
				return Integer.parseInt(value, 10);
			} catch (NumberFormatException ex) {
				throw new RuleParamNumberFormatException (ex, getId(), name, value);
			}
		}
	}
	
	protected String getString (Map<String, String> params, String name, boolean required, String defaultValue) {
		String value = params.get(name);
		if (StringUtils.isBlank(value)) {
			if (required) {
				throw new RuleParamNotFoundException (getId(), name, params);
			} else {
				return defaultValue;
			}
		} else {
			return value;
		}
	}
	
	protected RuleExecutionResult fail (RuleExecutionContext context, String code, Object... parameters) {
		String message = getMessage (context, code, parameters);
		return new FailRuleExecutionResult (message);
	}
	
	protected String getMessage(RuleExecutionContext context, String code, Object... parameters) {
		String key = String.format("%s.%s", getClass().getName(), code);
		return context.getMessage (key, parameters);
	}

	protected RuleExecutionResult ok() {
		return OKRuleExecutionResult.INSTANCE;
	}

}
