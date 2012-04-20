package net.cipol.core;

import java.util.List;

import net.cipol.api.FileService;
import net.cipol.api.PolicyService;
import net.cipol.model.Policy;
import net.cipol.model.PolicySummary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

@Service
public class PolicyCore implements PolicyService {

	private final FileService fileService;

	@Autowired
	public PolicyCore(FileService fileService) {
		this.fileService = fileService;
	}

	@Override
	@Cacheable("policy")
	public Policy loadPolicy(String policyId) {
		try {
			return fileService.read(Policy.class, policyId);
		} catch (CannotFindFileException ex) {
			throw new PolicyNotFoundException(policyId);
		}
	}
	
	@Override
	public List<PolicySummary> listPolicies() {
		return Lists.transform(fileService.readAll(Policy.class), new Function<Policy, PolicySummary>() {
			@Override
			public PolicySummary apply(Policy policy) {
				PolicySummary summary = new PolicySummary();
				summary.setUid(policy.getUid());
				summary.setName(policy.getName());
				summary.setDescription(policy.getDescription());
				return summary;
			}
		});
	}

}
