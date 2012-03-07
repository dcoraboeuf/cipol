package net.cipol.api;

import net.cipol.api.model.CommitInformation;
import net.cipol.api.model.ValidationResult;
import net.cipol.api.model.VersionInformation;

public interface API {
	
	VersionInformation getVersionInformation();
	
	ValidationResult validate (String policyId, CommitInformation information);

}
