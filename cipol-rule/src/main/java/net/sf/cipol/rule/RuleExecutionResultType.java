package net.sf.cipol.rule;

public enum RuleExecutionResultType {
	/**
	 * The rule was applied and it was a success.
	 */
	OK,
	/**
	 * The rule was applied with success but no further rule must apply.
	 */
	TERMINATE,
	/**
	 * The rule could not be applied.
	 */
	FAIL;
}
