package com.nnk.springboot.controllers;

import com.nnk.springboot.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller for handling login, secured views, and access errors.
 */
@Controller
@RequestMapping("app")
public class LoginController {

    private static final Logger logger = LogManager.getLogger(LoginController.class);

    @Autowired
    private UserRepository userRepository;

    /**
     * Displays the login form.
     *
     * @return login view
     */
    @GetMapping("login")
    public String login() {
        return "login";
    }

    /**
     * Displays the list of users for authenticated/authorized access only.
     *
     * @return view showing user list
     */
    @GetMapping("secure/article-details")
    public ModelAndView getAllUserArticles() {
        logger.info("Accessing secure article details (user list)");
        ModelAndView mav = new ModelAndView("user/list");
        mav.addObject("users", userRepository.findAll());
        return mav;
    }

    /**
     * Displays a 403 error page when user is not authorized.
     *
     * @return error view
     */
    @GetMapping("error")
    public ModelAndView error() {
        String errorMessage = "You are not authorized for the requested data.";
        logger.warn("Unauthorized access attempt - displaying 403 error page");
        ModelAndView mav = new ModelAndView("403");
        mav.addObject("errorMsg", errorMessage);
        return mav;
    }

    /**
     * Adds the currently authenticated user's username to the model.
     *
     * @param httpServletRequest the HTTP request
     * @return the remote user (username)
     */
    @ModelAttribute("remoteUser")
    public Object remoteUser(final HttpServletRequest httpServletRequest) {
        return httpServletRequest.getRemoteUser();
    }
}
