package net.sf.cipol.rule;

public enum RuleExecutionResultType {

	OK(true, false),

	TERMINATE(true, true),

	FAIL(false, true);

	private final boolean success;
	private final boolean abort;

	private RuleExecutionResultType(boolean success, boolean abort) {
		this.success = success;
		this.abort = abort;
	}

	public boolean success() {
		return success;
	}

	public boolean abort() {
		return abort;
	}

}
