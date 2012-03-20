package net.cipol.api;

import net.cipol.model.CommitInformation;
import net.cipol.model.ValidationReport;
import net.cipol.model.VersionInformation;

public interface APIService {
	
	VersionInformation getVersionInformation();
	
	ValidationReport validate (String policyId, CommitInformation information);

}
