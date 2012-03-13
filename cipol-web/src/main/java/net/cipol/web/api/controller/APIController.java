package net.cipol.web.api.controller;

import net.cipol.api.API;
import net.cipol.api.APIService;
import net.cipol.api.model.CommitInformation;
import net.cipol.api.model.ValidationResult;
import net.cipol.api.model.VersionInformation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api")
public class APIController implements API {

	private final APIService api;

	@Autowired
	public APIController(APIService api) {
		this.api = api;
	}

	@Override
	@RequestMapping(value = "/version", method = RequestMethod.GET)
	public @ResponseBody VersionInformation getVersionInformation() {
		return api.getVersionInformation();
	}
	
	@Override
	@RequestMapping(value = "/validate/{policyId}", method = RequestMethod.POST)
	public @ResponseBody ValidationResult validate(@PathVariable String policyId,
			@RequestBody CommitInformation information) {
		return api.validate(policyId, information);
	}

}
