package net.cipol.security.core;

import net.cipol.security.CipolRole;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

@Component
public class DefaultCipolAuthProvider implements AuthenticationProvider {

	@Override
	public Authentication authenticate(Authentication authentication)
			throws AuthenticationException {
		if (authentication instanceof UsernamePasswordAuthenticationToken) {
			UsernamePasswordAuthenticationToken o = (UsernamePasswordAuthenticationToken) authentication;
			if ("admin".equals(o.getName()) && "admin".equals((String)authentication.getCredentials())) {
				return new UsernamePasswordAuthenticationToken("admin", "admin", AuthorityUtils.createAuthorityList(CipolRole.ADMIN));
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
