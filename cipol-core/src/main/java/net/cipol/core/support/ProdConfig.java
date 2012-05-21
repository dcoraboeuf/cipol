package net.cipol.core.support;

import java.io.FileNotFoundException;

import net.cipol.CipolProfiles;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile(CipolProfiles.PROD)
public class ProdConfig extends AbstractConfig {

	public ProdConfig() {
		super("PROD", "classpath:/log4j_prod.properties");
	}
	
	@Override
	protected void initLogging() throws FileNotFoundException {
		System.setProperty("cipol.home", HomeSupport.home().getAbsolutePath());
		super.initLogging();
	}

}
