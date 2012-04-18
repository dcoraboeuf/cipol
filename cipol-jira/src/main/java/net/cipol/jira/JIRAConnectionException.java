package net.cipol.jira;

import net.cipol.model.support.CoreException;

public class JIRAConnectionException extends CoreException {
	
	public JIRAConnectionException (Exception ex, JIRAConfig config) {
		super (ex, config.getUrl(), config.getUser(), ex);
	}

}
