package com.nnk.springboot.controllers;

import com.nnk.springboot.model.RuleName;
import com.nnk.springboot.services.contracts.IRuleNameService;
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
 * Controller for managing {@link RuleName} entities.
 * Handles listing, creating, updating, and deleting RuleName records.
 */
@Controller
public class RuleNameController {

    private static final Logger logger = LogManager.getLogger(RuleNameController.class);
    private static final String REDIRECT_RULE_LIST = "redirect:/ruleName/list";

    @Autowired
    private IRuleNameService iRuleNameService;

    /**
     * Displays the list of all RuleName entities.
     *
     * @param model Spring MVC model
     * @return view name for rule list
     */
    @RequestMapping("/ruleName/list")
    public String listRuleNames(Model model) {
        model.addAttribute("ruleNames", iRuleNameService.getAllRuleNames());
        logger.info("Displaying rule name list");
        return "ruleName/list";
    }

    /**
     * Displays the form to add a new RuleName.
     *
     * @param ruleName empty RuleName object for form binding
     * @return view name for add form
     */
    @GetMapping("/ruleName/add")
    public String showAddRuleForm(RuleName ruleName) {
        logger.info("Displaying add rule name form");
        return "ruleName/add";
    }

    /**
     * Validates and saves a new RuleName entity.
     *
     * @param ruleName RuleName entity to save
     * @param result   validation result
     * @param model    Spring MVC model
     * @return redirect to list or return to form if errors
     */
    @PostMapping("/ruleName/validate")
    public String validateRuleName(@Valid RuleName ruleName, BindingResult result, Model model) {
        if (result.hasErrors()) {
            logger.warn("RuleName validation failed: {}", result.getAllErrors());
            return "ruleName/add";
        }

        iRuleNameService.saveRuleName(ruleName);
        logger.info("RuleName created: name={}, description={}", ruleName.getName(), ruleName.getDescription());

        return REDIRECT_RULE_LIST;
    }

    /**
     * Displays the form to update an existing RuleName.
     *
     * @param id    ID of the RuleName to update
     * @param model Spring MVC model
     * @return view name for update form
     */
    @GetMapping("/ruleName/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        RuleName ruleName = iRuleNameService.findRuleNameById(id)
                .orElseThrow(() -> {
                    logger.error("Invalid RuleName ID for update: {}", id);
                    return new IllegalArgumentException("Invalid RuleName ID: " + id);
                });

        model.addAttribute("ruleName", ruleName);
        logger.info("Displaying update form for RuleName ID: {}", id);
        return "ruleName/update";
    }

    /**
     * Validates and updates an existing RuleName entity.
     *
     * @param id        ID of the RuleName to update
     * @param ruleName  updated RuleName data from form
     * @param result    validation result
     * @param model     Spring MVC model
     * @return redirect to list or return to update form if errors
     */
    @PostMapping("/ruleName/update/{id}")
    public String updateRuleName(@PathVariable("id") Integer id, @Valid RuleName ruleName,
                                 BindingResult result, Model model) {
        if (result.hasErrors()) {
            logger.warn("RuleName update failed for ID {}: {}", id, result.getAllErrors());
            return "ruleName/update";
        }

        // Fetch existing RuleName
        RuleName existingRuleName = iRuleNameService.findRuleNameById(id)
                .orElseThrow(() -> {
                    logger.error("Invalid RuleName ID during update: {}", id);
                    return new IllegalArgumentException("Invalid RuleName ID: " + id);
                });

        // Update fields
        existingRuleName.setName(ruleName.getName());
        existingRuleName.setDescription(ruleName.getDescription());
        existingRuleName.setJson(ruleName.getJson());
        existingRuleName.setTemplate(ruleName.getTemplate());
        existingRuleName.setSqlStr(ruleName.getSqlStr());
        existingRuleName.setSqlPart(ruleName.getSqlPart());

        iRuleNameService.saveRuleName(existingRuleName);
        logger.info("RuleName updated: ID={}, name={}", id, ruleName.getName());

        return REDIRECT_RULE_LIST;
    }

    /**
     * Deletes an existing RuleName entity by ID.
     *
     * @param id    ID of the RuleName to delete
     * @param model Spring MVC model
     * @return redirect to list
     */
    @GetMapping("/ruleName/delete/{id}")
    public String deleteRuleName(@PathVariable("id") Integer id, Model model) {
        RuleName existingRuleName = iRuleNameService.findRuleNameById(id)
                .orElseThrow(() -> {
                    logger.error("Invalid RuleName ID for deletion: {}", id);
                    return new IllegalArgumentException("Invalid RuleName ID: " + id);
                });

        iRuleNameService.deleteRuleNameById(existingRuleName.getId());
        logger.info("RuleName deleted: ID={}, name={}", id, existingRuleName.getName());

        return REDIRECT_RULE_LIST;
    }

    /**
     * Adds the current logged-in username to the model.
     *
     * @param httpServletRequest the HTTP request
     * @return the remote user (username)
     */
    @ModelAttribute("remoteUser")
    public Object remoteUser(final HttpServletRequest httpServletRequest) {
        return httpServletRequest.getRemoteUser();
    }
}
