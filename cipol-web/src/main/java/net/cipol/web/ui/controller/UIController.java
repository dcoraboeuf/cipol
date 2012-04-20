package net.cipol.web.ui.controller;

import java.util.List;

import net.cipol.api.PolicyService;
import net.cipol.model.PolicySummary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
	public String home(Model model) {
		// Loads the policies
		List<PolicySummary> policies = policyService.listPolicies();
		model.addAttribute("policies", policies);
		// OK
		return "home";
	}

}
