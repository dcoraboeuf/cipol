package net.sf.cipol.rule.impl;

import java.util.Map;

import net.sf.cipol.rule.RuleExecutionContext;
import net.sf.cipol.rule.RuleExecutionResult;
import net.sf.cipol.rule.support.AbstractMessageRule;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class MessageLengthRule extends AbstractMessageRule<MessageLength> {
	
	public static final String RULE_ID = "message";

	public MessageLengthRule() {
		super(RULE_ID);
	}
	
	@Override
	public MessageLength createOptions(Map<String, String> parameters) {
		return new MessageLength(getInt(parameters, "maxlength", true, 0));
	}
	
	@Override
	protected RuleExecutionResult apply(RuleExecutionContext context, MessageLength options, String message) {
		if (StringUtils.length(message) < options.getMaxlength()) {
			return fail(context, "message", options.getMaxlength());
		} else {
			return ok();
		}
	}

	@Override
	public String getDescription(RuleExecutionContext context, MessageLength options) {
		return getMessage(context, "description", options.getMaxlength());
	}

}
