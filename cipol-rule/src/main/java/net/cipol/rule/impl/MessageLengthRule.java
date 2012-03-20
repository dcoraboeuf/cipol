package net.cipol.rule.impl;

import java.util.Map;

import net.cipol.model.CommitInformation;
import net.cipol.rule.RuleExecutionContext;
import net.cipol.rule.RuleExecutionResult;
import net.cipol.rule.support.AbstractRule;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class MessageLengthRule extends AbstractRule<MessageLength> {
	
	public static final String RULE_ID = "message";

	public MessageLengthRule() {
		super(RULE_ID);
	}
	
	@Override
	public MessageLength createOptions(Map<String, String> parameters) {
		return new MessageLength(getInt(parameters, "maxlength", true, 0));
	}
	
	@Override
	public RuleExecutionResult apply(RuleExecutionContext context,
			MessageLength options, CommitInformation commitInformation) {
		String message = commitInformation.getMessage();
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
