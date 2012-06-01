package net.cipol.web.ui.controller;

import java.util.List;

import net.cipol.api.PolicyService;
import net.cipol.model.PolicySummary;
import net.cipol.web.ui.page.HomePage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/ui")
public class UIController {
	
	private final PolicyService policyService;
	
	@Autowired
	public UIController(PolicyService policyService) {
		this.policyService = policyService;
	}

	@RequestMapping("/")
	public HomePage home() {
		// Loads the policies
		List<PolicySummary> policies = policyService.listPolicies();
		// OK
		return new HomePage (policies);
	}

}
