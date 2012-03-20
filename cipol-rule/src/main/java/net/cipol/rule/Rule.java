package net.cipol.rule;

import java.util.Map;

import net.cipol.model.CommitInformation;

public interface Rule<T> {
	
	// FIXME Description of parameters

	String getId();

	T createOptions(Map<String, String> parameters);
	
	String getDescription(RuleExecutionContext context, T options);
	
	RuleExecutionResult apply (RuleExecutionContext context, T options, CommitInformation commitInformation);

}
