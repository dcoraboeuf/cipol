package net.cipol.core;

import java.util.List;

import javax.annotation.PostConstruct;

import net.cipol.api.APIService;
import net.cipol.api.PolicyService;
import net.cipol.model.CommitInformation;
import net.cipol.model.Policy;
import net.cipol.model.RuleSet;
import net.cipol.model.ValidationResult;
import net.cipol.model.VersionInformation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
		List<RuleSet> rules = policy.getRules();
		for (RuleSet ruleSet : rules) {
			logger.debug("[validate] Getting rule set for path [{}]", ruleSet.getPath());
			// FIXME Is this rule set appliable?
			// if (isPathAppliable(ruleSet.getPath(), information.getPaths())) {
				// Applies this rule set
				
			// }
		}
		// FIXME Result
		ValidationResult result = new ValidationResult();
		result.setValid(true);
		return result;
	}

}
