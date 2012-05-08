package net.cipol.web.ui.controller;

import net.cipol.api.PolicyService;
import net.cipol.model.Policy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

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
	
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public View create(@RequestParam String name) {
		// Creates the policy
		String uid = policyService.createPolicy(name);
		// Redirects to the policy page
		return new RedirectView("/ui/policy/load/" + uid, true, false, false);
	}
	
	@RequestMapping(value = "/delete/{uid}", method = RequestMethod.GET)
	public View delete(@PathVariable String uid) {
		// Deletes the policy
		policyService.deletePolicy(uid);
		// Redirects to the home page
		return new RedirectView("/ui/", true, false, false);
	}

	@RequestMapping(value = "/update/{uid}", method = RequestMethod.POST)
	public void update (@PathVariable String uid, @RequestParam String fieldName, @RequestParam String value) {
		// Update
		policyService.updatePolicy (uid, fieldName, value);
		// FIXME Returns an acknowledgment
	}

}
