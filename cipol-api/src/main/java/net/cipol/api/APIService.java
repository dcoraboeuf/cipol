package net.cipol.api;

import net.cipol.model.CommitInformation;
import net.cipol.model.ValidationReport;
import net.cipol.model.VersionInformation;

public interface APIService {
	
	String DEFAULT_PATH = "**";
	
	VersionInformation getVersionInformation();
	
	ValidationReport validate (String policyId, CommitInformation information);

}
