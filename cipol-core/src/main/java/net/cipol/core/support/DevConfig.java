package net.cipol.core.support;

import net.cipol.CipolProfiles;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile(CipolProfiles.DEV)
public class DevConfig extends AbstractConfig {

	public DevConfig() {
		super("DEV", "classpath:/log4j_dev.properties");
	}

}