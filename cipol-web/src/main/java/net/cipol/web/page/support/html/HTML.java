package net.cipol.web.page.support.html;

import java.io.IOException;

public class HTML extends AbstractHTMLElement {
	
	@Override
	public void render(HTMLRenderer renderer) throws IOException {
		// DOCTYPE
		renderer.print ("<!DOCTYPE html>");
		// TODO Language
		renderer.print ("<html>");
		// TODO Head
		// TODO Body
		// End
		renderer.print ("</html>");
	}

}
