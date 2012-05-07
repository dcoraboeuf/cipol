package net.cipol.web.support.fm;

import java.util.List;
import java.util.UUID;

import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;

public class FnUuid implements TemplateMethodModel {

	@Override
	public Object exec(@SuppressWarnings("rawtypes") List list) throws TemplateModelException {
		return UUID.randomUUID().toString();
	}

}
