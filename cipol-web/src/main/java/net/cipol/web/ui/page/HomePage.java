package net.cipol.web.ui.page;

import java.util.List;

import net.cipol.model.PolicySummary;

public class HomePage extends BasicPage {

	private final List<PolicySummary> policies;

	public HomePage(List<PolicySummary> policies) {
		this.policies = policies;
	}

}
