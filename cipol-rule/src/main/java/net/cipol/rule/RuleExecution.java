package net.cipol.rule;

import net.cipol.model.CommitInformation;

public interface RuleExecution {

	String getDescription();

	RuleExecutionResult apply(CommitInformation information);

}
