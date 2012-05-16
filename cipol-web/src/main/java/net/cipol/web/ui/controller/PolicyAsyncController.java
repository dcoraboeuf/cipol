package net.cipol.web.ui.controller;

import java.util.Locale;

import net.cipol.api.PolicyService;
import net.cipol.model.support.CoreException;
import net.cipol.model.support.PolicyField;
import net.sf.jstring.Strings;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/ui/policy")
public class PolicyAsyncController {
	
	private final PolicyService policyService;
	private final Strings strings;
	
	@Autowired
	public PolicyAsyncController(PolicyService policyService, Strings strings) {
		this.policyService = policyService;
		this.strings = strings;
	}
	
	@ExceptionHandler(CoreException.class)
	public @ResponseBody String onException (CoreException ex) {
		return ex.getLocalizedMessage(strings, getLocale());
	}

	@RequestMapping(value = "/update/{uid}", method = RequestMethod.POST)
	public @ResponseBody String update (@PathVariable String uid, @RequestParam String fieldName, @RequestParam String value) {
		// Update
		policyService.updatePolicy (uid, PolicyField.valueOf(StringUtils.upperCase(fieldName)), value);
		// Returns an acknowledgment
		return "OK";
	}

	@RequestMapping(value = "/group/{uid}/create", method = RequestMethod.POST)
	public @ResponseBody String groupCreate (@PathVariable String uid, @RequestParam String name) {
		// Creates the group
		policyService.groupCreate (uid, name);
		// Returns an acknowledgment
		return "OK";
	}

	protected Locale getLocale() {
		// FIXME Uses a filter to set the locale
		return Locale.ENGLISH;
	}

}
