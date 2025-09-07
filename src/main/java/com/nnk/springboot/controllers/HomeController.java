package com.nnk.springboot.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller handling the home and admin landing pages.
 */
@Controller
public class HomeController {

	private static final Logger logger = LogManager.getLogger(HomeController.class);

	/**
	 * Displays the home page of the application.
	 *
	 * @param model Spring MVC model
	 * @return view name for the home page
	 */
	@RequestMapping("/")
	public String home(Model model) {
		logger.info("Accessing home page");
		return "home";
	}

	/**
	 * Redirects the admin user to the BidList list page after login.
	 *
	 * @param model Spring MVC model
	 * @return redirect URL to bid list
	 */
	@RequestMapping("/admin/home")
	public String adminHome(Model model) {
		logger.info("Redirecting admin to bid list");
		return "redirect:/bidList/list";
	}

	/**
	 * Adds the authenticated user's name to the model under 'remoteUser'.
	 *
	 * @param httpServletRequest the HTTP request
	 * @return username of the remote user
	 */
	@ModelAttribute("remoteUser")
	public Object remoteUser(final HttpServletRequest httpServletRequest) {
		return httpServletRequest.getRemoteUser();
	}
}
