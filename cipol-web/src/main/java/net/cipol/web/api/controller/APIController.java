package net.cipol.web.api.controller;

import java.util.Collections;
import java.util.Locale;

import net.cipol.api.API;
import net.cipol.api.APIService;
import net.cipol.api.model.CommitInformation;
import net.cipol.api.model.ValidationResult;
import net.cipol.api.model.VersionInformation;
import net.sf.jstring.LocalizableException;
import net.sf.jstring.Strings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api")
public class APIController implements API {

	private final APIService api;
	private final Strings strings;

	@Autowired
	public APIController(APIService api, Strings strings) {
		this.api = api;
		this.strings = strings;
	}

	@Override
	@RequestMapping(value = "/version", method = RequestMethod.GET)
	public @ResponseBody VersionInformation getVersionInformation() {
		return api.getVersionInformation();
	}
	
	@Override
	@RequestMapping(value = "/validate/{policyId}", method = RequestMethod.POST)
	public @ResponseBody ValidationResult validate(@PathVariable String policyId,
			@RequestBody CommitInformation information) {
		try {
			return api.validate(policyId, information);
		} catch (LocalizableException ex) {
			// TODO Obfuscate the error details
			// - uses CodeException with a code that is sent to the end user
			// - uses a UUID to 1) send to the user 2) trace in the logs
			ValidationResult result = new ValidationResult();
			result.setValid(false);
			result.setMessages(Collections.singletonList(ex.getLocalizedMessage(strings, getLocale())));
			return result;
		}
	}

	protected Locale getLocale() {
		// FIXME Uses a filter to set the locale
		return Locale.ENGLISH;
	}

}
