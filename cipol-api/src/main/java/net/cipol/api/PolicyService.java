package net.cipol.api;

import java.util.List;

import net.cipol.model.Policy;
import net.cipol.model.PolicySummary;
import net.cipol.model.support.PolicyField;

public interface PolicyService {

	Policy loadPolicy(String policyId);

	List<PolicySummary> listPolicies();

	String createPolicy(String name);

	void deletePolicy(String uid);

	void updatePolicy(String uid, PolicyField field, String value);

	String importPolicy(Policy policy, String name);

	void groupCreate(String uid, String name);

}
