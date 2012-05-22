package net.cipol.core.support;

import net.cipol.CipolProfiles;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile({CipolProfiles.TEST, CipolProfiles.IT})
public class TestConfig extends AbstractConfig {

	public TestConfig() {
		super("TEST", "classpath:log4j.properties");
	}

}