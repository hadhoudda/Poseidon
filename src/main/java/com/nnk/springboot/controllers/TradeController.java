package com.nnk.springboot.controllers;

import com.nnk.springboot.model.Trade;
import com.nnk.springboot.services.contracts.ITradeService;
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
 * Controller for managing {@link Trade} entities.
 * Provides endpoints for listing, adding, updating, and deleting trades.
 */
@Controller
public class TradeController {

    private static final Logger logger = LogManager.getLogger(TradeController.class);
    private static final String REDIRECT_TRADE_LIST = "redirect:/trade/list";

    @Autowired
    private ITradeService iTradeService;

    /**
     * Displays the list of all trades.
     *
     * @param model Spring MVC model
     * @return view name for trade list
     */
    @RequestMapping("/trade/list")
    public String listTrades(Model model) {
        model.addAttribute("trades", iTradeService.getAllTrades());
        logger.info("Displaying trade list");
        return "trade/list";
    }

    /**
     * Displays the form to add a new trade.
     *
     * @param trade empty Trade object to bind form
     * @return view name for trade addition
     */
    @GetMapping("/trade/add")
    public String showAddTradeForm(Trade trade) {
        logger.info("Displaying add trade form");
        return "trade/add";
    }

    /**
     * Validates and saves a new trade.
     *
     * @param trade  Trade object from the form
     * @param result result of validation
     * @param model  Spring MVC model
     * @return redirect to trade list if valid, else return to form
     */
    @PostMapping("/trade/validate")
    public String validateTrade(@Valid Trade trade, BindingResult result, Model model) {
        if (result.hasErrors()) {
            logger.warn("Trade validation failed: {}", result.getAllErrors());
            return "trade/add";
        }

        iTradeService.saveTrade(trade);
        logger.info("Trade created: account={}, type={}, quantity={}",
                trade.getAccount(), trade.getType(), trade.getBuyQuantity());

        return REDIRECT_TRADE_LIST;
    }

    /**
     * Displays the form to update an existing trade.
     *
     * @param id    trade ID
     * @param model Spring MVC model
     * @return view name for update form
     */
    @GetMapping("/trade/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        Trade trade = iTradeService.findTradeById(id)
                .orElseThrow(() -> {
                    logger.error("Invalid trade ID for update: {}", id);
                    return new IllegalArgumentException("Invalid trade ID: " + id);
                });

        model.addAttribute("trade", trade);
        logger.info("Displaying update form for trade ID: {}", id);
        return "trade/update";
    }

    /**
     * Validates and updates an existing trade.
     *
     * @param id     ID of the trade to update
     * @param trade  form data for update
     * @param result validation result
     * @param model  Spring MVC model
     * @return redirect to trade list if valid, else back to update form
     */
    @PostMapping("/trade/update/{id}")
    public String updateTrade(@PathVariable("id") Integer id, @Valid Trade trade,
                              BindingResult result, Model model) {
        if (result.hasErrors()) {
            logger.warn("Trade update failed for ID {}: {}", id, result.getAllErrors());
            return "trade/update";
        }

        // Retrieve and update the existing trade
        Trade existingTrade = iTradeService.findTradeById(id)
                .orElseThrow(() -> {
                    logger.error("Invalid trade ID during update: {}", id);
                    return new IllegalArgumentException("Invalid trade ID: " + id);
                });

        existingTrade.setAccount(trade.getAccount());
        existingTrade.setType(trade.getType());
        existingTrade.setBuyQuantity(trade.getBuyQuantity());

        iTradeService.saveTrade(existingTrade);
        logger.info("Trade updated: ID={}, account={}, type={}, quantity={}",
                id, trade.getAccount(), trade.getType(), trade.getBuyQuantity());

        return REDIRECT_TRADE_LIST;
    }

    /**
     * Deletes an existing trade by ID.
     *
     * @param id    ID of the trade to delete
     * @param model Spring MVC model
     * @return redirect to trade list
     */
    @GetMapping("/trade/delete/{id}")
    public String deleteTrade(@PathVariable("id") Integer id, Model model) {
        Trade existingTrade = iTradeService.findTradeById(id)
                .orElseThrow(() -> {
                    logger.error("Invalid trade ID for deletion: {}", id);
                    return new IllegalArgumentException("Invalid trade ID: " + id);
                });

        iTradeService.deleteTradeById(existingTrade.getTradeId());
        logger.info("Trade deleted: ID={}, account={}, type={}",
                id, existingTrade.getAccount(), existingTrade.getType());

        return REDIRECT_TRADE_LIST;
    }

    /**
     * Adds the current logged-in username to the model.
     *
     * @param httpServletRequest the HTTP request
     * @return the remote user
     */
    @ModelAttribute("remoteUser")
    public Object remoteUser(final HttpServletRequest httpServletRequest) {
        return httpServletRequest.getRemoteUser();
    }
}
