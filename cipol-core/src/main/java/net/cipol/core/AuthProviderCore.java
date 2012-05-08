package net.cipol.core;

import net.cipol.api.ConfigService;
import net.cipol.security.CipolAuthProviderSelector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class AuthProviderCore implements CipolAuthProviderSelector {
	
	private final ConfigService configService;

	@Autowired
	public AuthProviderCore(ConfigService configService) {
		this.configService = configService;
	}
	
	@Override
	@Cacheable("selectedAuthProviderId")
	public String getSelectedAuthProviderId() {
		// Loads parameter
		return configService.loadGeneralParameter ("authProviderId", false, "default");
	}

}
