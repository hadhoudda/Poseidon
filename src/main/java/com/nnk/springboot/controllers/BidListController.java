package com.nnk.springboot.controllers;

import com.nnk.springboot.model.BidList;
import com.nnk.springboot.services.contracts.IBidListService;
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
 * Controller for handling BidList CRUD operations.
 */
@Controller
public class BidListController {

    private static final Logger logger = LogManager.getLogger(BidListController.class);
    private static final String REDIRECT_BIDLIST_LIST = "redirect:/bidList/list";

    @Autowired
    private IBidListService iBidListService;

    /**
     * Displays the list of all bid lists.
     *
     * @param model Spring MVC model
     * @return view name for bid list
     */
    @RequestMapping("/bidList/list")
    public String home(Model model) {
        model.addAttribute("bidLists", iBidListService.getAllBidLists());
        logger.info("Displaying list of bid lists");
        return "bidList/list";
    }

    /**
     * Shows the form to add a new bid.
     *
     * @param bid BidList object for form binding
     * @return view name for the add form
     */
    @GetMapping("/bidList/add")
    public String addBidForm(BidList bid) {
        logger.info("Accessing bid list add form");
        return "bidList/add";
    }

    /**
     * Validates and saves a new bid list.
     *
     * @param bid   BidList object from form
     * @param result validation result
     * @return redirect or form view if errors
     */
    @PostMapping("/bidList/validate")
    public String validate(@Valid @ModelAttribute("bidList") BidList bid, BindingResult result) {
        if (result.hasErrors()) {
            logger.warn("BidList validation failed: {}", result.getAllErrors());
            return "bidList/add";
        }

        iBidListService.saveBidList(bid);
        logger.info("BidList created: account={}, type={}, quantity={}", bid.getAccount(), bid.getType(), bid.getBidQuantity());
        return REDIRECT_BIDLIST_LIST;
    }

    /**
     * Shows the update form for a given bid list.
     *
     * @param id bid list ID
     * @param model Spring MVC model
     * @return update form view
     */
    @GetMapping("/bidList/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        BidList bidList = iBidListService.findBidListById(id)
                .orElseThrow(() -> {
                    logger.error("Invalid bidList ID for update: {}", id);
                    return new IllegalArgumentException("Invalid bidList ID: " + id);
                });

        model.addAttribute("bidList", bidList);
        logger.info("Displaying update form for bidList ID: {}", id);
        return "bidList/update";
    }

    /**
     * Updates an existing bid list.
     *
     * @param id bid list ID
     * @param bidList updated data
     * @param result validation result
     * @param model Spring MVC model
     * @return redirect or update view if errors
     */
    @PostMapping("/bidList/update/{id}")
    public String updateBid(@PathVariable("id") Integer id,
                            @Valid @ModelAttribute("bidList") BidList bidList,
                            BindingResult result,
                            Model model) {
        if (result.hasErrors()) {
            logger.warn("BidList update failed for ID {}: {}", id, result.getAllErrors());
            return "bidList/update";
        }

        // On récupère l'objet existant
        BidList existingBid = iBidListService.findBidListById(id)
                .orElseThrow(() -> {
                    logger.error("Invalid BidList ID during update: {}", id);
                    return new IllegalArgumentException("Invalid BidList ID: " + id);
                });

        // Mise à jour des champs autorisés
        existingBid.setAccount(bidList.getAccount());
        existingBid.setType(bidList.getType());
        existingBid.setBidQuantity(bidList.getBidQuantity());

        iBidListService.saveBidList(existingBid);
        logger.info("BidList updated: ID={}, account={}, type={}, quantity={}",
                id, bidList.getAccount(), bidList.getType(), bidList.getBidQuantity());

        return REDIRECT_BIDLIST_LIST;
    }

    /**
     * Deletes a bid list by ID.
     *
     * @param id bid list ID
     * @param model Spring MVC model
     * @return redirect to list
     */
    @GetMapping("/bidList/delete/{id}")
    public String deleteBid(@PathVariable("id") Integer id, Model model) {
        BidList existingBid = iBidListService.findBidListById(id)
                .orElseThrow(() -> {
                    logger.error("Invalid BidList ID for deletion: {}", id);
                    return new IllegalArgumentException("Invalid BidList ID: " + id);
                });

        iBidListService.deleteBidListById(existingBid.getBidListId());
        logger.info("BidList deleted: ID={}, account={}, type={}",
                existingBid.getBidListId(), existingBid.getAccount(), existingBid.getType());

        model.addAttribute("bidLists", iBidListService.getAllBidLists());
        return REDIRECT_BIDLIST_LIST;
    }

    /**
     * Injects the current authenticated user in the model.
     *
     * @param httpServletRequest current HTTP request
     * @return the username of the remote user
     */
    @ModelAttribute("remoteUser")
    public Object remoteUser(final HttpServletRequest httpServletRequest) {
        return httpServletRequest.getRemoteUser();
    }
}
