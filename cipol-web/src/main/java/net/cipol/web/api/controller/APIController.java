package net.cipol.web.api.controller;

import net.cipol.api.API;
import net.cipol.api.APIService;
import net.cipol.api.model.VersionInformation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class APIController implements API {

	private final APIService api;

	@Autowired
	public APIController(APIService api) {
		this.api = api;
	}

	@Override
	@RequestMapping(value = "/version", method = RequestMethod.GET)
	public VersionInformation getVersionInformation() {
		return api.getVersionInformation();
	}

}
