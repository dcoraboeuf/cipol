package net.cipol.security.core;

import java.util.Map;

import net.cipol.security.CipolAuthProviderSelector;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class CipolAuthProvider implements AuthenticationProvider {

	private final Logger logger = LoggerFactory.getLogger(CipolAuthProvider.class);
	
	private final Map<String, AuthenticationProvider> providers;
	private final CipolAuthProviderSelector authProviderSelector;
	
	public CipolAuthProvider(Map<String, AuthenticationProvider> providers, CipolAuthProviderSelector authProviderSelector) {
		this.providers = providers;
		this.authProviderSelector = authProviderSelector;
	}

	@Override
	public Authentication authenticate(Authentication authentication)
			throws AuthenticationException {
		// Gets the provider
		AuthenticationProvider selectedProvider = getSelectedProvider();
		// Delegates
		return selectedProvider.authenticate(authentication);
		// return new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), authentication.getCredentials(), AuthorityUtils.createAuthorityList(CipolRole.ADMIN));
	}

	protected AuthenticationProvider getSelectedProvider() {
		logger.debug("Selecting the provider...");
		// Gets the selected provider ID
		String selectedProviderId = getSelectedProviderId();
		logger.debug("Selecting the provider with ID = {}...", selectedProviderId);
		// Gets the selected provider
		AuthenticationProvider selectedProvider = providers.get(selectedProviderId);
		if (selectedProvider == null) {
			throw new IllegalStateException("Unknown provider: " + selectedProviderId);
		}
		// OK
		logger.debug("Selected provider is {}...", selectedProvider);
		return selectedProvider;
	}

	protected String getSelectedProviderId() {
		String id = authProviderSelector.getSelectedAuthProviderId();
		if (StringUtils.isBlank(id)) {
			throw new IllegalStateException("Selected provider is blank or null");
		} else {
			return id;
		}
	}

	@Override
	public boolean supports(Class<?> authentication) {
		// Gets the provider
		AuthenticationProvider selectedProvider = getSelectedProvider();
		// Tests
		return selectedProvider.supports(authentication);
	}

}
