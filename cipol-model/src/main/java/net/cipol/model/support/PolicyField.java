package net.cipol.model.support;

public enum PolicyField {
	
	NAME,
	
	DESCRIPTION;

	public String getPropertyName() {
		return name().toLowerCase();
	}

}
