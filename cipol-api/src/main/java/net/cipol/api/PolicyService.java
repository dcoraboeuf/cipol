package net.cipol.api;

import java.util.List;

import net.cipol.model.Policy;
import net.cipol.model.PolicySummary;

public interface PolicyService {

	Policy loadPolicy(String policyId);

	List<PolicySummary> listPolicies();

}
