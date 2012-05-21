package net.cipol.web.support;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import net.cipol.CipolProfiles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.util.Log4jConfigurer;

public class InitListener implements ServletContextListener {
	
	private final Logger logger = LoggerFactory.getLogger("CONFIG");

	@Override
	public void contextDestroyed(ServletContextEvent e) {
	}

	@Override
	public void contextInitialized(ServletContextEvent e) {
		initLogging("classpath:/log4j_default.properties");
		logger.info("Initializing log configuration...");
		// Gets the current profile
		GenericApplicationContext context = new GenericApplicationContext();
		context.refresh();
		Set<String> profiles = new HashSet<String>(Arrays.asList(context.getEnvironment().getActiveProfiles()));
		logger.info("Profiles = {}", profiles);
		// Logging profile
		String loggingProfile = CipolProfiles.DEV;
		if (profiles.contains(CipolProfiles.PROD)) {
			loggingProfile = CipolProfiles.PROD;
		}
		logger.info("Using logging profile: {}", loggingProfile);
		// Logging config path
		String loggingConfigPath = String.format("classpath:/log4j_%s.properties", loggingProfile);
		logger.info("Using logging configuration path: {}", loggingConfigPath);
		// Log initialization
		initLogging(loggingConfigPath);
	}

	protected void initLogging(String path) {
		try {
			Log4jConfigurer.initLogging(path);
		} catch (FileNotFoundException ex) {
			throw new RuntimeException("Cannot initialize logging", ex);
		}
	}

}
