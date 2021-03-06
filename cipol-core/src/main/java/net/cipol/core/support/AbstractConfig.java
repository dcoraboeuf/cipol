package net.cipol.core.support;

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
		initGeneral();
		initLogging();
	}

	protected void initLogging() throws FileNotFoundException {
		Log4jConfigurer.initLogging(loggingPath);
	}

	protected void initGeneral() {
		logger.info("Using {} configuration", configName);
	}

}
