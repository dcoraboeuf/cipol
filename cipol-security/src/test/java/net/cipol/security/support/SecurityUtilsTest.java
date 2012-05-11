package net.cipol.security.support;

import static net.cipol.security.CipolRole.ADMIN;
import static net.cipol.security.CipolRole.USER;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;

public class SecurityUtilsTest {
	
	@Before
	public void clear() {
		SecurityContextHolder.clearContext();
	}
	
	@Test
	public void isUserInRole_admin_not_authenticated() {
		assertFalse(SecurityUtils.isUserInRole(ADMIN));
	}
	
	@Test
	public void isUserInRole_user_not_authenticated() {
		assertFalse(SecurityUtils.isUserInRole(USER));
	}
	
	@Test
	public void isUserInRole_admin_for_user() {
		connectAsUser();
		assertFalse(SecurityUtils.isUserInRole(ADMIN));
	}
	
	@Test
	public void isUserInRole_user_for_user() {
		connectAsUser();
		assertTrue(SecurityUtils.isUserInRole(USER));
	}
	
	@Test
	public void isUserInRole_admin_for_admin() {
		connectAsAdmin();
		assertTrue(SecurityUtils.isUserInRole(ADMIN));
	}
	
	@Test
	public void isUserInRole_user_for_admin() {
		connectAsAdmin();
		assertTrue(SecurityUtils.isUserInRole(USER));
	}
	
	@Test
	public void isAdmin_not_authenticated() {
		assertFalse(SecurityUtils.isAdmin());
	}
	
	@Test
	public void isAdmin_for_user() {
		connectAsUser();
		assertFalse(SecurityUtils.isAdmin());
	}
	
	@Test
	public void isAdmin_for_admin() {
		connectAsAdmin();
		assertTrue(SecurityUtils.isAdmin());
	}
	
	protected void connectAsAdmin() {
		connectAs(ADMIN,USER);
	}
	
	protected void connectAsUser() {
		connectAs(USER);
	}

	protected void connectAs(String... roles) {
		SecurityContextImpl securityContext = new SecurityContextImpl();
		securityContext.setAuthentication(new UsernamePasswordAuthenticationToken(
				"name", "pwd", AuthorityUtils.createAuthorityList(roles)));
		SecurityContextHolder.setContext(securityContext);
	}

}
