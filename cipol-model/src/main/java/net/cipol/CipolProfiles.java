package net.cipol;

public interface CipolProfiles {

	/**
	 * Real environments
	 */
	String PROD = "prod";
	
	/**
	 * Automated tests running against a deployed application
	 */
	String IT = "it";
	
	/**
	 * Unit tests
	 */
	String TEST = "test";
	
	/**
	 * Development mode, from within an IDE
	 */
	String DEV = "dev";

}
