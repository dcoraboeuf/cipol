package net.cipol.web.support;

import java.io.FileNotFoundException;

import javax.annotation.PostConstruct;

import net.cipol.CipolProfiles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile({CipolProfiles.TEST, CipolProfiles.IT, CipolProfiles.DEV})
public class DevConfig {
	
	private final Logger logger = LoggerFactory.getLogger("CONFIG");

	@PostConstruct
	public void init() throws FileNotFoundException {
		//Log4jConfigurer.initLogging("classpath:/log4j_dev.properties");
		logger.info("Using DEV configuration");		
	}

}
