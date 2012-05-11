package net.cipol.security;

/**
 * Security roles in CIPOL.
 *
 */
public interface CipolRole {
	
	/**
	 * General administrator - can do anything.
	 */
	String ADMIN = "ROLE_ADMIN";
	
	/**
	 * General authenticated user
	 */
	String USER = "ROLE_USER";
	
	/**
	 * Anonymous user - used for the API access
	 */
	String ANONYMOUS = "IS_AUTHENTICATED_ANONYMOUSLY";

}
