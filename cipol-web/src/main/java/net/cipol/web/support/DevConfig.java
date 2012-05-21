package net.cipol.web.support;

import net.cipol.CipolProfiles;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile({CipolProfiles.TEST, CipolProfiles.IT, CipolProfiles.DEV})
public class DevConfig extends AbstractConfig {

	public DevConfig() {
		super("DEV", "classpath:/log4j_dev.properties");
	}

}