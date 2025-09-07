package com.nnk.springboot.controllers;

import com.nnk.springboot.model.CurvePoint;
import com.nnk.springboot.services.contracts.ICurvePointService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for managing CurvePoint entities.
 */
@Controller
public class CurveController {

    private static final Logger logger = LogManager.getLogger(CurveController.class);
    private static final String REDIRECT_CURVEPOINT_LIST = "redirect:/curvePoint/list";

    @Autowired
    private ICurvePointService iCurvePointService;

    /**
     * Displays the list of all curve points.
     *
     * @param model Spring MVC model
     * @return view name for curvePoint list
     */
    @RequestMapping("/curvePoint/list")
    public String listCurvePoints(Model model) {
        model.addAttribute("curvePoints", iCurvePointService.getAllCurvePoints());
        logger.info("Displaying curve point list");
        return "curvePoint/list";
    }

    /**
     * Displays the form to add a new curve point.
     *
     * @param curvePoint empty curve point object for form binding
     * @return view name for add form
     */
    @GetMapping("/curvePoint/add")
    public String addCurvePointForm(CurvePoint curvePoint) {
        logger.info("Accessing curve point add form");
        return "curvePoint/add";
    }

    /**
     * Validates and saves a new curve point.
     *
     * @param curvePoint curve point from form
     * @param result validation result
     * @param model Spring MVC model
     * @return redirect to list or return to form if validation fails
     */
    @PostMapping("/curvePoint/validate")
    public String validateCurvePoint(@Valid CurvePoint curvePoint, BindingResult result, Model model) {
        if (result.hasErrors()) {
            logger.warn("Curve point validation failed: {}", result.getAllErrors());
            return "curvePoint/add";
        }

        iCurvePointService.saveCurvePoint(curvePoint);
        logger.info("Curve point created: curveId={}, term={}, value={}",
                curvePoint.getCurveId(), curvePoint.getTerm(), curvePoint.getValue());
        return REDIRECT_CURVEPOINT_LIST;
    }

    /**
     * Displays the update form for an existing curve point.
     *
     * @param id curve point ID
     * @param model Spring MVC model
     * @return view name for update form
     */
    @GetMapping("/curvePoint/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        CurvePoint curvePoint = iCurvePointService.findCurvePointById(id)
                .orElseThrow(() -> {
                    logger.error("Invalid curvePoint ID for update: {}", id);
                    return new IllegalArgumentException("Invalid curvePoint ID: " + id);
                });

        model.addAttribute("curvePoint", curvePoint);
        logger.info("Displaying update form for curvePoint ID: {}", id);
        return "curvePoint/update";
    }

    /**
     * Updates an existing curve point.
     *
     * @param id curve point ID
     * @param curvePoint updated data from form
     * @param result validation result
     * @param model Spring MVC model
     * @return redirect to list or return to form if validation fails
     */
    @PostMapping("/curvePoint/update/{id}")
    public String updateCurvePoint(@PathVariable("id") Integer id, @Valid CurvePoint curvePoint,
                                   BindingResult result, Model model) {
        if (result.hasErrors()) {
            logger.warn("Curve point update failed for ID {}: {}", id, result.getAllErrors());
            return "curvePoint/update";
        }

        CurvePoint existingCurvePoint = iCurvePointService.findCurvePointById(id)
                .orElseThrow(() -> {
                    logger.error("Invalid curvePoint ID during update: {}", id);
                    return new IllegalArgumentException("Invalid curvePoint ID: " + id);
                });

        // Mise à jour des champs autorisés
        existingCurvePoint.setTerm(curvePoint.getTerm());
        existingCurvePoint.setValue(curvePoint.getValue());

        iCurvePointService.saveCurvePoint(existingCurvePoint);
        logger.info("Curve point updated: ID={}, term={}, value={}", id, curvePoint.getTerm(), curvePoint.getValue());

        return REDIRECT_CURVEPOINT_LIST;
    }

    /**
     * Deletes a curve point by ID.
     *
     * @param id curve point ID
     * @param model Spring MVC model
     * @return redirect to curve point list
     */
    @GetMapping("/curvePoint/delete/{id}")
    public String deleteCurvePoint(@PathVariable("id") Integer id, Model model) {
        CurvePoint existingCurvePoint = iCurvePointService.findCurvePointById(id)
                .orElseThrow(() -> {
                    logger.error("Invalid curvePoint ID for deletion: {}", id);
                    return new IllegalArgumentException("Invalid curvePoint ID: " + id);
                });

        iCurvePointService.deleteCurvePointById(existingCurvePoint.getId());
        logger.info("Curve point deleted: ID={}, term={}, value={}", id, existingCurvePoint.getTerm(), existingCurvePoint.getValue());

        return REDIRECT_CURVEPOINT_LIST;
    }

    /**
     * Adds the remote user (currently authenticated user) to the model.
     *
     * @param httpServletRequest current HTTP request
     * @return the username of the authenticated user
     */
    @ModelAttribute("remoteUser")
    public Object remoteUser(final HttpServletRequest httpServletRequest) {
        return httpServletRequest.getRemoteUser();
    }
}
