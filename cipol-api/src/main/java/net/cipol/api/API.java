package net.cipol.api;

import net.cipol.model.CommitInformation;
import net.cipol.model.ValidationResult;
import net.cipol.model.VersionInformation;

public interface API {
	
	VersionInformation getVersionInformation();
	
	ValidationResult validate (String policyId, CommitInformation information);

}
