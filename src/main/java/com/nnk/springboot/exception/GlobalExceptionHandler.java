package com.nnk.springboot.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * Global exception handler for the application.
 * <p>
 * This class uses {@link ControllerAdvice} to catch and handle exceptions thrown
 * across all controllers in a centralized way.
 * </p>
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles {@link IllegalArgumentException} thrown anywhere in the application.
     *
     * @param ex    the exception thrown
     * @param model the model used to pass data to the view
     * @return the error view name
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgument(IllegalArgumentException ex, Model model) {
        // Add the exception message to the model so it can be displayed on the error page
        model.addAttribute("errorMessage", ex.getMessage());
        // Return a custom error page located at templates/error/404.html
        return "error/404";
    }

    /**
     * Handles 404 errors (no matching handler found for the request).
     *
     * @param ex    the exception thrown when no handler is found
     * @param model the model used to pass data to the view
     * @return the error view name
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public String handle404(NoHandlerFoundException ex, Model model) {
        // Provide a generic "Page not found" message
        model.addAttribute("errorMessage", "Page not found");
        return "error/404";
    }
}
