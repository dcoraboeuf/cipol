package net.cipol.web.support;

import java.io.FileNotFoundException;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Log4jConfigurer;

abstract class AbstractConfig {

	private final Logger logger = LoggerFactory.getLogger("CONFIG");

	private final String configName;
	private final String loggingPath;

	protected AbstractConfig(String configName, String loggingPath) {
		this.configName = configName;
		this.loggingPath = loggingPath;
	}

	@PostConstruct
	public void init() throws FileNotFoundException {
		Log4jConfigurer.initLogging(loggingPath);
		logger.info("Using {} configuration", configName);
	}

}
