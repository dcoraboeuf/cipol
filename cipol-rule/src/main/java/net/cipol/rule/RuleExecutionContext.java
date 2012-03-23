package net.cipol.rule;

public interface RuleExecutionContext {

	String getMessage(String key, Object... parameters);

	boolean isMemberOfGroup(String author, String group);

}
