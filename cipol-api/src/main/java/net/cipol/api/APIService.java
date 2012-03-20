package net.cipol.api;

import net.cipol.model.CommitInformation;
import net.cipol.model.ValidationResult;
import net.cipol.model.VersionInformation;

public interface APIService {
	
	VersionInformation getVersionInformation();
	
	ValidationResult validate (String policyId, CommitInformation information);

}
