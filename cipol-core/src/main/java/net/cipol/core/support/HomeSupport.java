package net.cipol.core.support;

import java.io.File;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class HomeSupport {

	private static final Logger log = LoggerFactory
			.getLogger(HomeSupport.class);

	public static final String ENV_HOME = "CIPOL_HOME";
	public static final String SYSTEM_HOME = "cipol.home";
	public static final String HOME_DIRECTORY = "cipol";

	public static File home() {
		File home;
		// Gets the home directory
		String sysHome = System.getProperty(SYSTEM_HOME);
		String envHome = System.getenv(ENV_HOME);
		if (StringUtils.isNotBlank(sysHome)) {
			log.info("Getting home directory from system property");
			home = new File(sysHome).getAbsoluteFile();
		} else if (StringUtils.isNotBlank(envHome)) {
			log.info("Getting home directory from environment variable");
			home = new File(envHome).getAbsoluteFile();
		} else {
			log.info("Getting home directory from user home");
			home = new File(System.getProperty("user.home"), HOME_DIRECTORY)
					.getAbsoluteFile();
		}
		log.info("Home at {}", home);
		return home;
	}
	
	private HomeSupport() {
	}

}
