package net.cipol.api;

import java.util.List;

import net.cipol.model.ParamValue;
import net.cipol.rule.RuleExecution;
import net.cipol.rule.RuleExecutionContext;

public interface RuleService {

	<T> RuleExecution getRule(RuleExecutionContext context, String ruleId, List<ParamValue> parameters);

}
