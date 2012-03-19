package net.cipol.core;

import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;

import net.cipol.api.APIService;
import net.cipol.api.PolicyService;
import net.cipol.model.CommitInformation;
import net.cipol.model.Policy;
import net.cipol.model.RuleDefinition;
import net.cipol.model.RuleExecutionResult;
import net.cipol.model.RuleSet;
import net.cipol.model.ValidationResult;
import net.cipol.model.VersionInformation;
import net.sf.cipol.rule.RuleExecutionResultType;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

@Service
public class APICore implements APIService {
	
	private final Logger logger = LoggerFactory.getLogger(APIService.class);

	private final String versionNumber;
	private final PolicyService policyService;
	
	@Autowired
	public APICore(@Value("${app.version}") String versionNumber, PolicyService policyService) {
		this.versionNumber = versionNumber;
		this.policyService = policyService;
	}
	
	@PostConstruct
	public void log() {
		logger.info("Version {}", versionNumber);
	}

	@Override
	public VersionInformation getVersionInformation() {
		VersionInformation versionInformation = new VersionInformation();
		// Version number
		versionInformation.setVersionNumber(versionNumber);
		// OK
		return versionInformation;
	}
	
	@Override
	public ValidationResult validate(String policyId,
			CommitInformation information) {
		logger.debug("[validate] Request for policy [{}]", policyId);
		// Loads the policy definition
		Policy policy = policyService.loadPolicy (policyId);
		logger.debug("[validate] Applying policy [{}]", policy.getName());
		// For each rule of the policy
		ValidationResult validationResult = new ValidationResult();
		List<RuleSet> rules = policy.getRules();
		for (RuleSet ruleSet : rules) {
			logger.debug("[validate] Getting rule set for path [{}]", ruleSet.getPath());
			// Is this rule set appliable?
			if (isPathAppliable(ruleSet.getPath(), information.getPaths())) {
				// Applies this rule set
				RuleExecutionResult result = applyRuleSet (ruleSet, information);
				// Overall result
				RuleExecutionResultType resultType = getResultType(result);
				switch (resultType) {
				case OK:
					// Going on
					break;
				case TERMINATE:
					// Done, exits the validation with a success
					validationResult.setValid(true);
					return validationResult;
				case FAIL:
					// Returns the validation result with a failure message
					validationResult.setValid(false);
					validationResult.setMessages(Collections.singletonList(result.getMessage()));
					return validationResult;
				default:
					throw new IllegalStateException("Unknown result type: " + resultType);
				}
			}
		}
		// Result OK
		validationResult.setValid(true);
		return validationResult;
	}

	protected RuleExecutionResultType getResultType(RuleExecutionResult result) {
		if (result.isSuccess()) {
			Boolean terminate = result.getTerminate();
			if (terminate != null && terminate) {
				return RuleExecutionResultType.TERMINATE;
			} else {
				return RuleExecutionResultType.OK;
			}
		} else {
			return RuleExecutionResultType.FAIL;
		}
	}

	protected RuleExecutionResult applyRuleSet(RuleSet ruleSet,
			CommitInformation information) {
		logger.debug("[validate] Applying rule set for path [{}]", ruleSet.getPath());
		// For each rule
		for (RuleDefinition ruleDefinition: ruleSet.getRules()) {
			RuleExecutionResult result = applyRule (ruleDefinition, information);
			RuleExecutionResultType resultType = getResultType(result);
			switch (resultType) {
			case OK:
				// Going on
				break;
			case TERMINATE:
			case FAIL:
				// Terminates here
				return result;
			default:
				throw new IllegalStateException("Unknown result type: " + resultType);
			}
		}
		// OK
		return ok();
	}

	protected RuleExecutionResult ok() {
		RuleExecutionResult result = new RuleExecutionResult();
		result.setSuccess(true);
		return result;
	}

	protected RuleExecutionResult applyRule(RuleDefinition ruleDefinition,
			CommitInformation information) {
		// FIXME Result
		return ok();
	}

	protected boolean isPathAppliable(final String path, List<String> paths) {
		String candidate = Iterables.find(paths, new Predicate<String>() {
			
			@Override
			public boolean apply(String value) {
				return StringUtils.startsWith(value, path);
			}

		}, null);
		return candidate != null;
	}

}
