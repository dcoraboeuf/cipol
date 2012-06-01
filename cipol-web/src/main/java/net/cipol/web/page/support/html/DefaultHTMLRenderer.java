package net.cipol.web.page.support.html;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

public class DefaultHTMLRenderer implements HTMLRenderer {

	private final PrintWriter writer;

	public DefaultHTMLRenderer(HttpServletResponse response) throws IOException {
		writer = response.getWriter();
	}
	
	@Override
	public void print(String line, Object... args) {
		writer.format(line, args);
	}

	public void close() {
		writer.close();
	}

}
