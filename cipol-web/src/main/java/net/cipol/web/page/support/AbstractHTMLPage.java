package net.cipol.web.page.support;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class AbstractHTMLPage extends AbstractPage {
	
	@Override
	public String getContentType() {
		return "text/html";
	}
	
	@Override
	public void render(Map<String, ?> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
