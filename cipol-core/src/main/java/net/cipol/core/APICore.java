package net.cipol.core;

import net.cipol.api.APIService;
import net.cipol.api.model.VersionInformation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class APICore implements APIService {

	private final String versionNumber;
	
	@Autowired
	public APICore(@Value("${app.version}") String versionNumber) {
		this.versionNumber = versionNumber;
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
