package net.cipol.core.support;

import java.io.File;

import net.cipol.CipolProfiles;
import net.cipol.api.HomeService;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile({CipolProfiles.TEST})
public class HomeTestService implements HomeService {

	private final File home;

	public HomeTestService() {
		home = new File("src/test/resources/home");
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
