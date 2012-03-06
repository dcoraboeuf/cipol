package net.cipol.core;

import javax.annotation.PostConstruct;

import net.cipol.api.APIService;
import net.cipol.api.model.VersionInformation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class APICore implements APIService {
	
	private final Logger logger = LoggerFactory.getLogger(APIService.class);

	private final String versionNumber;
	
	@Autowired
	public APICore(@Value("${app.version}") String versionNumber) {
		this.versionNumber = versionNumber;
	}
	
	@PostConstruct
	public void log() {
		logger.info("Version {}", versionNumber);
	}

	@Override
	public VersionInformation getVersionInformation() {
		VersionInformation versionInformation = new VersionInformation();
		// Version number
		versionInformation.setVersionNumber(versionNumber);
		// OK
		return versionInformation;
	}

}
