package net.cipol.web.ui.controller;

import java.util.Locale;
import java.util.UUID;

import net.cipol.api.PolicyService;
import net.cipol.model.support.CoreException;
import net.cipol.model.support.PolicyField;
import net.sf.jstring.Strings;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	
	private static final String ACK_OK = "OK";

	private final Logger errors = LoggerFactory.getLogger("User");
	
	private final PolicyService policyService;
	private final Strings strings;
	
	@Autowired
	public PolicyAsyncController(PolicyService policyService, Strings strings) {
		this.policyService = policyService;
		this.strings = strings;
	}
	
	// TODO Moves this handler in a more generic location
	@ExceptionHandler(CoreException.class)
	public @ResponseBody String onException (CoreException ex) {
		// Generates a UUID
		String uuid = UUID.randomUUID().toString();
		// Traces the error
		// TODO Adds request information
		// TODO Adds authentication information
		// TODO Custom logging for Prod
		errors.error(String.format("[%s] %s", uuid, ex.getLocalizedMessage(strings, Locale.ENGLISH)));
		// Returns a message to display to the user
		return strings.get(getLocale(), "general.error", ex, uuid);
	}

	@RequestMapping(value = "/update/{uid}", method = RequestMethod.POST)
	public @ResponseBody String update (@PathVariable String uid, @RequestParam String fieldName, @RequestParam String value) {
		// Update
		policyService.updatePolicy (uid, PolicyField.valueOf(StringUtils.upperCase(fieldName)), value);
		// Returns an acknowledgment
		return ACK_OK;
	}

	@RequestMapping(value = "/group/{uid}/create", method = RequestMethod.POST)
	public @ResponseBody String groupCreate (@PathVariable String uid, @RequestParam String name) {
		// Creates the group
		policyService.groupCreate (uid, name);
		// Returns an acknowledgment
		return ACK_OK;
	}

	@RequestMapping(value = "/group/{uid}/delete/{name}", method = RequestMethod.GET)
	public @ResponseBody String groupDelete (@PathVariable String uid, @PathVariable String name) {
		// Deletes the group
		policyService.groupDelete (uid, name);
		// Returns an acknowledgment
		return ACK_OK;
	}

	protected Locale getLocale() {
		// FIXME Uses a filter to set the locale
		return Locale.ENGLISH;
	}

}
