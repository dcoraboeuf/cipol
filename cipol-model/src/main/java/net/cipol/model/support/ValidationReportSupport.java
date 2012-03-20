package net.cipol.model.support;

import net.cipol.model.ValidationReport;

public class ValidationReportSupport {

	public static ValidationReport createErrorReport(String message) {
		ValidationReport report = new ValidationReport();
		report.setSuccess(false);
		report.setMessage(message);
		return report;
	}

}
