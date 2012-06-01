package net.cipol.web.page.support;

import net.cipol.web.page.Page;

import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

public class PageMethodReturnValueHandler implements HandlerMethodReturnValueHandler {

	@Override
	public boolean supportsReturnType(MethodParameter methodparameter) {
		return Page.class.isAssignableFrom(methodparameter.getParameterType());
	}

	@Override
	public void handleReturnValue(Object obj, MethodParameter methodparameter,
			ModelAndViewContainer modelandviewcontainer,
			NativeWebRequest nativewebrequest) throws Exception {
		modelandviewcontainer.setView((Page) obj);
	}

}
