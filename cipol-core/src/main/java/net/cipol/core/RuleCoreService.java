package net.cipol.core;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import net.cipol.api.RuleService;
import net.cipol.model.CommitInformation;
import net.cipol.model.ParamValue;
import net.cipol.rule.Rule;
import net.cipol.rule.RuleExecution;
import net.cipol.rule.RuleExecutionContext;
import net.cipol.rule.RuleExecutionResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

@Service
public class RuleCoreService implements RuleService {
	
	private final Map<String, Rule<?>> ruleIndex;
	
	@Autowired
	public RuleCoreService(Collection<Rule<?>> rules) {
		ruleIndex = Maps.uniqueIndex(rules, new Function<Rule<?>, String>() {
			@Override
			public String apply(Rule<?> rule) {
				return rule.getId();
			}
		});
	}

	@Override
	public <T> RuleExecution getRule(final RuleExecutionContext context, String ruleId, List<ParamValue> parameters) {
		Map<String, String> params;
		if (parameters != null) {
			// Indexes the parameters
			ImmutableMap<String, ParamValue> paramsEntries = Maps.uniqueIndex(parameters, new Function<ParamValue, String>() {
				@Override
				public String apply(ParamValue param) {
					return param.getName();
				}
			});
			params = Maps.transformValues(paramsEntries, new Function<ParamValue, String>() {
				@Override
				public String apply(ParamValue param) {
					return param.getValue();
				}
			});
		} else {
			params = Collections.emptyMap();
		}
		// Gets the rule itself
		final Rule<T> rule = getRule(ruleId);
		// Creates the options
		final T options = rule.createOptions(params);
		// Creates the execution
		return new RuleExecution() {
			
			@Override
			public String getDescription() {
				return rule.getDescription(context, options);
			}
			
			@Override
			public RuleExecutionResult apply(CommitInformation information) {
				return rule.apply(context, options, information);
			}
		};
	}

	protected <T> Rule<T> getRule(String ruleId) {
		@SuppressWarnings("unchecked")
		Rule<T> rule = (Rule<T>) ruleIndex.get(ruleId);
		if (rule != null) {
			return rule;
		} else {
			throw new RuleNotFoundException (ruleId);
		}
	}

}
