package net.cipol.core;

import java.util.List;

import javax.annotation.PostConstruct;

import net.cipol.api.APIService;
import net.cipol.api.PolicyService;
import net.cipol.api.RuleService;
import net.cipol.model.CommitInformation;
import net.cipol.model.Policy;
import net.cipol.model.RuleDefinition;
import net.cipol.model.RuleSet;
import net.cipol.model.ValidationDetail;
import net.cipol.model.ValidationReport;
import net.cipol.model.VersionInformation;
import net.sf.cipol.rule.RuleExecution;
import net.sf.cipol.rule.RuleExecutionResult;
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
	private final RuleService ruleService;
	
	@Autowired
	public APICore(@Value("${app.version}") String versionNumber, PolicyService policyService, RuleService ruleService) {
		this.versionNumber = versionNumber;
		this.policyService = policyService;
		this.ruleService = ruleService;
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
	public ValidationReport validate(String policyId,
			CommitInformation information) {
		logger.debug("[validate] Request for policy [{}]", policyId);
		// Loads the policy definition
		Policy policy = policyService.loadPolicy (policyId);
		logger.debug("[validate] Applying policy [{}]", policy.getName());
		// For each rule of the policy
		ValidationReport validationReport = new ValidationReport();
		List<RuleSet> rules = policy.getRules();
		for (RuleSet ruleSet : rules) {
			logger.debug("[validate] Getting rule set for path [{}]", ruleSet.getPath());
			// Is this rule set appliable?
			if (isPathAppliable(ruleSet.getPath(), information.getPaths())) {
				// Applies this rule set
				boolean abort = applyRuleSet (validationReport, ruleSet, information);
				// Aborting?
				if (abort) {
					logger.debug("[validate] Aborting after applying rule set for path [{}]", ruleSet.getPath());
					break;
				}
			}
		}
		// Consolidation
		consolidate(validationReport);
		// Result
		return validationReport;
	}

	protected void consolidate(ValidationReport validationReport) {
		validationReport.setSuccess(true);
		validationReport.setMessage(null);
		List<ValidationDetail> details = validationReport.getDetails();
		for (ValidationDetail detail : details) {
			if (!detail.isSuccess()) {
				validationReport.setSuccess(false);
				validationReport.setMessage(detail.getMessage());
				break;
			}
		}
	}

	protected boolean applyRuleSet(ValidationReport validationReport, RuleSet ruleSet,
			CommitInformation information) {
		// Path
		String ruleSetPath = ruleSet.getPath();
		logger.debug("[validate] Applying rule set for path [{}]", ruleSetPath);
		// For each rule
		for (RuleDefinition ruleDefinition: ruleSet.getRules()) {
			// Prepare the report detail
			ValidationDetail detail = new ValidationDetail();
			detail.setPath(ruleSetPath);
			// Applies the rule
			boolean abort = applyRule (detail, ruleDefinition, information);
			if (abort) {
				return true;
			}
		}
		// Ran OK
		return false;
	}

	protected boolean applyRule(ValidationDetail detail,
			RuleDefinition ruleDefinition, CommitInformation information) {
		String ruleId = ruleDefinition.getRuleId();
		// Logging
		logger.debug("[validate] Applying rule [{}] for path [{}]", ruleId, detail.getPath());
		// Gets the rule
		RuleExecution rule = ruleService.getRule (ruleId, ruleDefinition.getParameters());
		// Applies the rule
		RuleExecutionResult result = rule.apply (information);
		// Adds rule execution details into the report
		RuleExecutionResultType executionType = result.getType();
		detail.setRuleId(ruleId);
		detail.setRuleDescription(rule.getDescription());
		detail.setSuccess(executionType.success());
		detail.setMessage(result.getMessage());
		// Status -> fail or terminate ==> abort
		return executionType.abort();
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
