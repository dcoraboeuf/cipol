package net.cipol.web.api.controller;

import java.util.Locale;

import net.cipol.CipolLoggers;
import net.cipol.api.APIService;
import net.cipol.model.CommitInformation;
import net.cipol.model.ValidationReport;
import net.cipol.model.VersionInformation;
import net.cipol.model.support.ValidationReportSupport;
import net.cipol.web.ui.locale.CurrentLocale;
import net.sf.jstring.LocalizableException;
import net.sf.jstring.Strings;

import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api")
public class APIController {
	
	private final Logger audit = LoggerFactory.getLogger(CipolLoggers.API_AUDIT);

	private final APIService api;
	private final Strings strings;
	private final CurrentLocale currentLocale;

	@Autowired
	public APIController(APIService api, Strings strings, CurrentLocale currentLocale) {
		this.api = api;
		this.strings = strings;
		this.currentLocale = currentLocale;
	}

	@RequestMapping(value = "/version", method = RequestMethod.GET)
	public @ResponseBody VersionInformation getVersionInformation() {
		return api.getVersionInformation();
	}
	
	@RequestMapping(value = "/validate/{policyId}", method = RequestMethod.POST)
	public @ResponseBody ValidationReport validate(@PathVariable String policyId,
			@RequestBody CommitInformation information) {
		ValidationReport report;
		// Gets the report
		try {
			report = api.validate(policyId, information);
		} catch (LocalizableException ex) {
			// FIXME Uses the standard error controller (see PolicyCore)
			// TODO Obfuscate the error details
			// - uses CodeException with a code that is sent to the end user
			// - uses a UUID to 1) send to the user 2) trace in the logs
			report = ValidationReportSupport.createErrorReport(ex.getLocalizedMessage(strings, getLocale()));
		}
		// Audit
		audit(policyId, information, report);
		// OK
		return report;
	}

	protected void audit(String policyId, CommitInformation information, ValidationReport validationReport) {
		// Message
		// TODO Id of the commit information (tx number for SVN for example)
		String message = String.format("policy=%s,author=%s,success=%s,message=%s",
				policyId,
				information.getAuthor(),
				validationReport.isSuccess(),
				ObjectUtils.toString(validationReport.getMessage(), ""));
		// Logging
		audit.info(message);
	}

	protected Locale getLocale() {
		return currentLocale.getCurrentLocale();
	}

}
