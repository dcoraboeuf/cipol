package net.cipol.security;

import org.springframework.security.authentication.AuthenticationProvider;

public interface CipolAuthenticationProvider extends AuthenticationProvider {

	String getId();

}
