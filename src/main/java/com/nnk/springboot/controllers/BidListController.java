package com.nnk.springboot.controllers;

import com.nnk.springboot.model.BidList;
import com.nnk.springboot.services.contracts.IBidListService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@Controller
public class BidListController {
    @Autowired
    private IBidListService iBidListService;

    @RequestMapping("/bidList/list")
    public String home(Model model) {
        model.addAttribute("bidLists", iBidListService.getAllBidLists());
        return "bidList/list";
    }

    @GetMapping("/bidList/add")
    public String addBidForm(BidList bid) {
        return "bidList/add";
    }


    @PostMapping("/bidList/validate")
    public String validate(@Valid @ModelAttribute("bidList") BidList bid, BindingResult result) {
        if (result.hasErrors()) {
            return "bidList/add";
        }
        iBidListService.saveBidList(bid);
        return "redirect:/bidList/list";
    }

    @GetMapping("/bidList/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        BidList bidList = iBidListService.findBidListById(id).orElseThrow(() -> new IllegalArgumentException("Invalid bidList Id:" + id));
        model.addAttribute("bidList", bidList);
            return "bidList/update";
    }

    @PostMapping("/bidList/update/{id}")
    public String updateBid(@PathVariable("id") Integer id,
                            @Valid @ModelAttribute("bidList") BidList bidList,
                            BindingResult result,
                            Model model) {

        if (result.hasErrors()) {
            return "bidList/update";
        }

        // On récupère le BidList existant
        BidList existingBid = iBidListService.findBidListById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid BidList ID: " + id));

        // Mise à jour des champs modifiables
        existingBid.setAccount(bidList.getAccount());
        existingBid.setType(bidList.getType());
        existingBid.setBidQuantity(bidList.getBidQuantity());

        iBidListService.saveBidList(existingBid);

        return "redirect:/bidList/list";
    }


    @GetMapping("/bidList/delete/{id}")
    public String deleteBid(@PathVariable("id") Integer id, Model model) {
        // On récupère le BidList existant
        BidList existingBid = iBidListService.findBidListById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid BidList ID: " + id));
        iBidListService.deleteBidListById(existingBid.getBidListId());
        model.addAttribute("bidLists", iBidListService.getAllBidLists());
        return "redirect:/bidList/list";
    }

    @ModelAttribute("remoteUser")
    public Object remoteUser(final HttpServletRequest httpServletRequest) {
        return httpServletRequest.getRemoteUser();
    }
}
