package net.cipol.web.support.fm;

import java.util.List;

import net.cipol.web.ui.locale.CurrentLocale;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;

import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;

public class FnLocaleSelected implements TemplateMethodModel {
	
	private final CurrentLocale currentLocale;
	
	@Autowired
	public FnLocaleSelected(CurrentLocale currentLocale) {
		this.currentLocale = currentLocale;
	}

	@Override
	public Object exec(@SuppressWarnings("rawtypes") List list) throws TemplateModelException {
		// Checks
		Validate.notNull(list, "List of arguments is required");
		Validate.notEmpty(list, "List of arguments must not be empty");
		// Gets the value
		String value = (String) list.get(0);
		// Gets the current locale value
		String currentValue = currentLocale.getCurrentLocale().toString();
		// OK?
		return StringUtils.startsWithIgnoreCase(currentValue, value);
	}

}
