package net.cipol.web.ui.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/ui")
public class UIController {
	
	@RequestMapping("/")
	public String home() {
		// OK
		return "home";
	}

}
