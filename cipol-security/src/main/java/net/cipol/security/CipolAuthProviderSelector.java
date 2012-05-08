package net.cipol.security;

public interface CipolAuthProviderSelector {

	String getSelectedAuthProviderId();
	
	void setSelectedAuthProvidedId (String id);

}
