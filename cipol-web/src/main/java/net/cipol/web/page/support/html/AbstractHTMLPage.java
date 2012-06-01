package net.cipol.web.page.support.html;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.cipol.web.page.support.AbstractPage;

public abstract class AbstractHTMLPage extends AbstractPage {
	
	@Override
	public String getContentType() {
		return "text/html";
	}
	
	@Override
	public void render(Map<String, ?> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// Creates the HTML component
		HTML html = createHTML(model, request);
		// Renders the HTML component
		render (html, response);
	}

	protected void render(HTML html, HttpServletResponse response) throws IOException {
		// Renderer
		DefaultHTMLRenderer renderer = new DefaultHTMLRenderer (response);
		try {
			// Rendering
			html.render (renderer);
		} finally {
			renderer.close();
		}
	}

	protected HTML createHTML(Map<String, ?> model, HttpServletRequest request) {
		HTML html = new HTML();
		// FIXME Collects information
		// OK
		return html;
	}

}
