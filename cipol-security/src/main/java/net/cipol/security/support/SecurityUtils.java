package net.cipol.security.support;

import net.cipol.security.CipolRole;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Miscellaneous utility methods for dealing with the security context.
 * 
 */
public final class SecurityUtils {

	/**
	 * Utility class that cannot be instantiated.
	 */
	private SecurityUtils() {
	}

	/**
	 * Is the current authenticated user an administrator?
	 * 
	 * @return <code>true</code> if the current user is an administrator,
	 *         <code>false</code> if not authenticated or not an administrator
	 * @see #isUserInRole(String)
	 * @see CipolRole#ADMIN
	 */
	public static boolean isAdmin() {
		return isUserInRole(CipolRole.ADMIN);
	}

	/**
	 * Is the current authenticated user granted the requested role?
	 * 
	 * @return <code>true</code> if the current user is granted the requested role
	 *         <code>false</code> if not authenticated or not granted this role
	 * @see CipolRole
	 */
	public static boolean isUserInRole(String role) {

		SecurityContext context = SecurityContextHolder.getContext();

		Authentication authentication = context.getAuthentication();
		if (authentication == null) {
			return false;
		}

		for (GrantedAuthority auth : authentication.getAuthorities()) {
			if (role.equals(auth.getAuthority())) {
				return true;
			}
		}

		return false;
	}

}
