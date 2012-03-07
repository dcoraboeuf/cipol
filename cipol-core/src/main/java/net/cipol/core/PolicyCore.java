package net.cipol.core;

import net.cipol.api.FileService;
import net.cipol.api.PolicyService;
import net.cipol.api.model.Policy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

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
		return fileService.read (Policy.class, policyId);
	}

}
