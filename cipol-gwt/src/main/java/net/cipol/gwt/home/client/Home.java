package net.cipol.gwt.home.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Home implements EntryPoint {

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		GWT.log("Loading home page...");
		DockLayoutPanel p = new DockLayoutPanel(Unit.EM);
		p.addNorth(createHeader(), 2);
		p.addSouth(new HTML("footer"), 2);
		//p.addWest(new HTML("west"), 2);
		p.add(new HTML("My content"));
		RootPanel.get().add(p);
	}

	private HTML createHeader() {
		HTML html = new HTML("header");
		html.addStyleName("header");
		return html;
	}
}
