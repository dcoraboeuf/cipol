package net.cipol.core;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import net.cipol.security.CipolAuthProviderSelector;

@Service
public class AuthProviderCore extends AbstractDaoService implements CipolAuthProviderSelector {

	@Autowired
	public AuthProviderCore(DataSource dataSource) {
		super(dataSource);
	}
	
	@Override
	@Cacheable("selectedAuthProviderId")
	public String getSelectedAuthProviderId() {
		// FIXME Reads from the database
		return "default";
	}

}
