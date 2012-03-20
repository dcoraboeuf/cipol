package net.cipol.api;

import java.util.List;

import net.cipol.model.ParamValue;
import net.cipol.rule.RuleExecution;

public interface RuleService {

	<T> RuleExecution getRule(String ruleId, List<ParamValue> parameters);

}
