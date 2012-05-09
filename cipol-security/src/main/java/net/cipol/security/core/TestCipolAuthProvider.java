package net.cipol.security.core;

import java.util.HashMap;
import java.util.Map;

import net.cipol.security.CipolAuthenticationProvider;
import net.cipol.security.CipolRole;

import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

import com.google.common.collect.ImmutableMap;

@Component
@Profile({ "dev", "it" })
public class TestCipolAuthProvider implements CipolAuthenticationProvider {
	
	private final Map<String, String> roles;
	
	public TestCipolAuthProvider() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("admin", CipolRole.ADMIN);
		map.put("user1", CipolRole.USER);
		map.put("user2", CipolRole.USER);
		roles = ImmutableMap.copyOf(map);
	}
	
	@Override
	public String getId() {
		return "test";
	}

	@Override
	public Authentication authenticate(Authentication authentication)
			throws AuthenticationException {
		if (authentication instanceof UsernamePasswordAuthenticationToken) {
			UsernamePasswordAuthenticationToken o = (UsernamePasswordAuthenticationToken) authentication;
			String name = o.getName();
			String role = roles.get(name);
			if (role != null) {
				if ("test".equals((String)authentication.getCredentials())) {
					return new UsernamePasswordAuthenticationToken(name, "test", AuthorityUtils.createAuthorityList(role));
				}
			}
			return authentication;
		} else {
			return null;
		}
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
	}

}
