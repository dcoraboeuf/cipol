package net.cipol.web.ui.controller;

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import net.cipol.api.PolicyService;
import net.cipol.model.Policy;
import net.cipol.model.support.PolicyField;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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

	@RequestMapping(value = "/export/{uid}", method = RequestMethod.GET)
	public void export (HttpServletResponse response, @PathVariable String uid) throws IOException {
		// Loads the policy
		Policy policy = policyService.loadPolicy(uid);
		// JSON generation
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(policy);
		// JSON as UTF-8
		byte[] utf8 = json.getBytes("UTF-8");
		// Exports as JSON
    	response.setContentType("text/json");
    	response.addHeader("Content-Disposition", String.format("attachment; filename=\"policy-%s-%s.json\"", uid, policy.getName()));
    	response.setContentLength(utf8.length);
    	// Gets the output
    	ServletOutputStream out = response.getOutputStream();
    	// Writes to the output
    	out.write(utf8);
    	out.flush();
    	out.close();
    	// No response (already handled)
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
	public @ResponseBody String update (@PathVariable String uid, @RequestParam String fieldName, @RequestParam String value) {
		// Update
		policyService.updatePolicy (uid, PolicyField.valueOf(StringUtils.upperCase(fieldName)), value);
		// Returns an acknowledgment
		return "OK";
	}

}
