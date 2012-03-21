package net.cipol.rule.impl;

import java.util.Set;

import net.sf.jstring.Localizable;
import net.sf.jstring.LocalizableMessage;
import net.sf.jstring.NonLocalizable;

import org.apache.commons.lang.StringUtils;

import com.google.common.collect.ImmutableSet;

public class AuthorizationRuleOptions {

	private static final String SEPARATOR = ",";
	
	private final Set<String> allowed;
	private final Set<String> disallowed;
	
	public AuthorizationRuleOptions(String allowed, String disallowed) {
		this.allowed = ImmutableSet.copyOf(StringUtils.split(allowed, SEPARATOR));
		this.disallowed = ImmutableSet.copyOf(StringUtils.split(disallowed, SEPARATOR));
	}

	public Localizable getAllowedString() {
		return getAllowedString (allowed);
	}

	public Localizable getDisallowedString() {
		return getAllowedString (disallowed);
	}

	protected Localizable getAllowedString(Set<String> authors) {
		if (authors.isEmpty()) {
			return new LocalizableMessage(AuthenticatedRule.class.getName() + ".none");
		} else {
			return new NonLocalizable(StringUtils.join(authors, SEPARATOR));
		}
	}

	public boolean isAllowed(String author) {
		return allowed.contains(author) && !disallowed.contains(author);
	}

}
