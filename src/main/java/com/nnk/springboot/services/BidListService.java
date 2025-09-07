package com.nnk.springboot.services;

import com.nnk.springboot.model.BidList;
import com.nnk.springboot.repositories.BidListRepository;
import com.nnk.springboot.services.contracts.IBidListService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service implementation for managing BidList entities.
 */
@Service
public class BidListService implements IBidListService {

    private final BidListRepository bidListRepository;

    /**
     * Constructor for BidListService.
     *
     * @param bidListRepository the repository used for BidList entity operations
     */
    public BidListService(BidListRepository bidListRepository) {
        this.bidListRepository = bidListRepository;
    }

    /**
     * Save a BidList entity.
     *
     * @param bidList the BidList entity to save
     * @return the saved BidList entity
     */
    @Override
    public BidList saveBidList(BidList bidList) {
        return bidListRepository.save(bidList);
    }

    /**
     * Retrieve all BidList entities.
     *
     * @return a list of all BidList entities
     */
    @Override
    public List<BidList> getAllBidLists() {
        return bidListRepository.findAll();
    }

    /**
     * Find a BidList entity by its id.
     *
     * @param id the id of the BidList entity
     * @return an Optional containing the found BidList or empty if not found
     */
    @Override
    public Optional<BidList> findBidListById(Integer id) {
        return bidListRepository.findById(id);
    }

    /**
     * Update a BidList entity by id.
     *
     * @param id      the id of the BidList to update
     * @param bidList the BidList entity containing updated data
     */
    @Override
    public void updateBidList(Integer id, BidList bidList) {
        bidList.setBidListId(id);
        bidListRepository.save(bidList);
    }

    /**
     * Delete a BidList entity by id.
     *
     * @param id the id of the BidList to delete
     */
    @Override
    public void deleteBidListById(Integer id) {
        bidListRepository.deleteById(id);
    }
}
