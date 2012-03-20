package net.cipol.core;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import net.cipol.api.RuleService;
import net.cipol.model.CommitInformation;
import net.cipol.model.ParamValue;
import net.sf.cipol.rule.Rule;
import net.sf.cipol.rule.RuleExecution;
import net.sf.cipol.rule.RuleExecutionContext;
import net.sf.cipol.rule.RuleExecutionResult;
import net.sf.jstring.Strings;

@Service
public class RuleCoreService implements RuleService {
	
	private final Map<String, Rule<?>> ruleIndex;
	private final Strings strings;
	
	@Autowired
	public RuleCoreService(Collection<Rule<?>> rules, Strings strings) {
		ruleIndex = Maps.uniqueIndex(rules, new Function<Rule<?>, String>() {
			@Override
			public String apply(Rule<?> rule) {
				return rule.getId();
			}
		});
		this.strings = strings;
	}

	@Override
	public <T> RuleExecution getRule(String ruleId, List<ParamValue> parameters) {
		// Indexes the parameters
		ImmutableMap<String, ParamValue> paramsEntries = Maps.uniqueIndex(parameters, new Function<ParamValue, String>() {
			@Override
			public String apply(ParamValue param) {
				return param.getName();
			}
		});
		Map<String, String> params = Maps.transformValues(paramsEntries, new Function<ParamValue, String>() {
			@Override
			public String apply(ParamValue param) {
				return param.getValue();
			}
		});
		// Execution context
		final RuleExecutionContext context = new RuleExecutionContext() {
			
			@Override
			public String getMessage(String key, Object... parameters) {
				return strings.get(getLocale(), key, parameters);
			}
		};
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

	protected Locale getLocale() {
		// FIXME Uses a filter to set the locale (see APIController)
		return Locale.ENGLISH;
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
