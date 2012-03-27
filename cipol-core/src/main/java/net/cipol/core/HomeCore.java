package net.cipol.core;

import java.io.File;

import net.cipol.CipolProfiles;
import net.cipol.api.HomeService;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile({ CipolProfiles.PROD, CipolProfiles.DEV, CipolProfiles.IT })
public class HomeCore implements HomeService {

	private final File home;

	public HomeCore() {
		home = HomeSupport.home();
	}

	@Override
	public File getHome() {
		return home;
	}

	@Override
	public File getFile(String path) {
		return new File(home, path);
	}

}
