package net.cipol.core;

import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;

import net.cipol.CipolLoggers;
import net.cipol.api.APIService;
import net.cipol.api.PolicyService;
import net.cipol.api.RuleService;
import net.cipol.model.CommitInformation;
import net.cipol.model.Group;
import net.cipol.model.Policy;
import net.cipol.model.RuleDefinition;
import net.cipol.model.RuleSet;
import net.cipol.model.ValidationDetail;
import net.cipol.model.ValidationReport;
import net.cipol.model.VersionInformation;
import net.cipol.rule.RuleExecution;
import net.cipol.rule.RuleExecutionContext;
import net.cipol.rule.RuleExecutionResult;
import net.cipol.rule.RuleExecutionResultType;
import net.sf.jstring.Strings;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

@Service
public class APICore implements APIService {

	private final Logger logger = LoggerFactory.getLogger(APIService.class);
	private final Logger audit = LoggerFactory.getLogger(CipolLoggers.API_AUDIT);
	
	private final PathMatcher pathMatcher = new AntPathMatcher();

	private final String versionNumber;
	private final PolicyService policyService;
	private final RuleService ruleService;
	private final Strings strings;
	
	@Autowired
	public APICore(@Value("${app.version}") String versionNumber, PolicyService policyService, RuleService ruleService, Strings strings) {
		this.versionNumber = versionNumber;
		this.policyService = policyService;
		this.ruleService = ruleService;
		this.strings = strings;
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
		// Validation report to return
		ValidationReport validationReport = new ValidationReport();
		try {
			logger.debug("[validate] Request for policy [{}]", policyId);
			// Loads the policy definition
			final Policy policy = policyService.loadPolicy (policyId);
			logger.debug("[validate] Applying policy [{}]", policy.getName());
			// Execution context
			final RuleExecutionContext context = new RuleExecutionContext() {
				
				@Override
				public String getMessage(String key, Object... parameters) {
					return strings.get(getLocale(), key, parameters);
				}
				
				@Override
				public boolean isMemberOfGroup(final String author, final String group) {
					return isMemberOfPolicyGroup(policy, author, group);
				}
			};
			// For each rule of the policy
			List<RuleSet> rules = policy.getRules();
			for (RuleSet ruleSet : rules) {
				// Path pattern
				String ruleSetPath = ruleSet.getPath();
				if (StringUtils.isBlank(ruleSetPath)) {
					ruleSetPath = DEFAULT_PATH;
				}
				// Disabled?
				if (ruleSet.isDisabled()) {
					logger.debug("[validate] Rule set for path [{}] is disabled", ruleSetPath);
					continue;
				}
				logger.debug("[validate] Checking rule set for path [{}]", ruleSetPath);
				// Is this rule set appliable?
				if (isPathAppliable(ruleSetPath, information.getPaths())) {
					// Applies this rule set
					boolean abort = applyRuleSet (context, validationReport, ruleSet, information);
					// Aborting?
					if (abort) {
						logger.debug("[validate] Aborting after applying rule set for path [{}]", ruleSetPath);
						break;
					}
				}
			}
			// Consolidation
			consolidate(validationReport);
			// Result
			return validationReport;
		} finally {
			// Audit message
			audit (policyId, information, validationReport);
		}
	}

	protected void audit(String policyId, CommitInformation information, ValidationReport validationReport) {
		// Message
		// TODO Id of the commit information (tx number for SVN for example)
		String message = String.format("policy=%s,author=%s,success=%s,message=%s",
				policyId,
				information.getAuthor(),
				validationReport.isSuccess(),
				ObjectUtils.toString(validationReport.getMessage(), ""));
		// Logging
		audit.info(message);
	}

	protected Locale getLocale() {
		// FIXME Uses a filter to set the locale (see APIController)
		return Locale.ENGLISH;
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

	protected boolean applyRuleSet(RuleExecutionContext context, ValidationReport validationReport, RuleSet ruleSet,
			CommitInformation information) {
		// Path
		String ruleSetPath = ruleSet.getPath();
		logger.debug("[validate] Applying rule set for path [{}]", ruleSetPath);
		// For each rule
		for (RuleDefinition ruleDefinition: ruleSet.getRules()) {
			// Disabled?
			if (ruleDefinition.isDisabled()) {
				logger.debug("[validate] Rule [{}] for path [{}] is disabled", ruleDefinition.getRuleId(), ruleSet.getPath());
				continue;
			}
			// Prepare the report detail
			ValidationDetail detail = new ValidationDetail();
			detail.setPath(ruleSetPath);
			// Applies the rule
			boolean abort = applyRule (context, detail, ruleDefinition, information);
			// Adding the details
			validationReport.addDetails(detail);
			// Aborting the rules
			if (abort) {
				return true;
			}
		}
		// Ran OK
		return false;
	}

	protected boolean applyRule(RuleExecutionContext context, ValidationDetail detail,
			RuleDefinition ruleDefinition, CommitInformation information) {
		String ruleId = ruleDefinition.getRuleId();
		// Logging
		logger.debug("[validate] Applying rule [{}] for path [{}]", ruleId, detail.getPath());
		// Gets the rule
		RuleExecution rule = ruleService.getRule (context, ruleId, ruleDefinition.getParameters());
		// Applies the rule
		RuleExecutionResult result = rule.apply (information);
		// Adds rule execution details into the report
		RuleExecutionResultType executionType = result.getType();
		detail.setRuleId(ruleId);
		detail.setRuleDescription(rule.getDescription());
		detail.setSuccess(executionType.success());
		detail.setMessage(result.getMessage());
		// Logging
		logger.debug("[validate] success = {}, message = {}", detail.isSuccess(), detail.getMessage());
		// Status -> fail or terminate ==> abort
		return executionType.abort();
	}

	protected boolean isPathAppliable(final String ruleSetPath, List<String> paths) {
		String candidate = Iterables.find(paths, new Predicate<String>() {
			
			@Override
			public boolean apply(String value) {
				return pathMatcher.match(ruleSetPath, value);
			}

		}, null);
		return candidate != null;
	}

	protected boolean isMemberOfPolicyGroup(final Policy policy,
			final String author, final String group) {
		// Policy groups
		List<Group> groups = policy.getGroups();
		boolean policyGroup = Iterables.any(groups, new Predicate<Group>() {
			@Override
			public boolean apply(Group input) {
				return StringUtils.equals(group, input.getName())
						&& Iterables.contains(input.getMembers(), author);
			}});
		// TODO Gets the global security groups
		// OK
		return policyGroup;
	}

}
