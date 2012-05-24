package net.cipol.security.core;

import net.cipol.security.CipolAuthenticationProvider;
import net.cipol.security.CipolRole;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

@Component
public class DefaultCipolAuthProvider implements CipolAuthenticationProvider {
	
	private static final String DEFAULT_USER = "admin";
	private static final String DEFAULT_PASSWORD = DEFAULT_USER;

	@Override
	public String getId() {
		return "default";
	}

	@Override
	public Authentication authenticate(Authentication authentication)
			throws AuthenticationException {
		if (authentication instanceof UsernamePasswordAuthenticationToken) {
			UsernamePasswordAuthenticationToken o = (UsernamePasswordAuthenticationToken) authentication;
			if (DEFAULT_USER.equals(o.getName()) && DEFAULT_PASSWORD.equals((String)authentication.getCredentials())) {
				return new UsernamePasswordAuthenticationToken(DEFAULT_USER, DEFAULT_PASSWORD, AuthorityUtils.createAuthorityList(CipolRole.ADMIN));
			} else {
				return authentication;
			}
		} else {
			return null;
		}
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
	}

}
