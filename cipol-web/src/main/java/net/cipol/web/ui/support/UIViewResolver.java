package net.cipol.web.ui.support;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

import com.google.common.collect.Maps;

public class UIViewResolver implements ViewResolver {
	
	private final ViewResolver delegateViewResolver;
	private final String defaultMapping;
	private final Map<String, String> mapping;
	
	public UIViewResolver(ViewResolver delegateViewResolver, String defaultMapping, Map<String, String> mapping) {
		this.delegateViewResolver = delegateViewResolver;
		this.defaultMapping = defaultMapping;
		this.mapping = mapping;
	}

	@Override
	public View resolveViewName(final String name, Locale locale) throws Exception {
		String targetName = mapping.get(name);
		if (StringUtils.isBlank(targetName)) {
			targetName = defaultMapping;
		}
		final View view = delegateViewResolver.resolveViewName(targetName, locale);
		return new View() {

			@Override
			public void render(Map<String, ?> map,
					HttpServletRequest httpservletrequest,
					HttpServletResponse httpservletresponse) throws Exception {
				HashMap<String, Object> tmp = Maps.newHashMap(map);
				tmp.put("viewName", name);
				view.render(tmp, httpservletrequest, httpservletresponse);
			}

			@Override
			public String getContentType() {
				return view.getContentType();
			}
		};
	}

}
