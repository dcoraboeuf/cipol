package net.cipol.web.ui.controller;

import net.cipol.api.PolicyService;
import net.cipol.model.Policy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/ui/policy")
public class PolicyController {
	
	private final PolicyService policyService;
	
	@Autowired
	public PolicyController(PolicyService policyService) {
		this.policyService = policyService;
	}

	@RequestMapping(value = "/load/{uid}", method = RequestMethod.GET)
	public String load (@PathVariable String uid, Model model) {
		// Loads the policy
		Policy policy = policyService.loadPolicy(uid);
		model.addAttribute("policy", policy);
		// OK
		return "policy";
	}

}
