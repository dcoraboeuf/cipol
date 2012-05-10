package net.cipol.web.support.fm;

import java.util.List;

import net.cipol.security.support.SecurityUtils;

import org.apache.commons.lang3.Validate;

import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;

public class FnRoleCheck implements TemplateMethodModel {

	@Override
	public Boolean exec(@SuppressWarnings("rawtypes") List list) throws TemplateModelException {
		// Checks
		Validate.notNull(list, "List of arguments is required");
		Validate.notEmpty(list, "List of arguments must not be empty");
		// Gets the role
		String role = (String) list.get(0);
		// Checks
		return SecurityUtils.isUserInRole(role);
	}

}