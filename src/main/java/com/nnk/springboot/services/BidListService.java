package com.nnk.springboot.services;

import com.nnk.springboot.model.BidList;
import com.nnk.springboot.repositories.BidListRepository;
import com.nnk.springboot.services.contracts.IBidListService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BidListService implements IBidListService {

    private final BidListRepository bidListRepository;

    public BidListService(BidListRepository bidListRepository) {
        this.bidListRepository = bidListRepository;
    }

    @Override
    public BidList saveBidList(BidList bidList) {
        return bidListRepository.save(bidList);
    }

    @Override
    public List<BidList> getAllBidLists() {
        return bidListRepository.findAll();
    }

    @Override
    public Optional<BidList> findBidListById(Integer id) {
        return bidListRepository.findById(id);
    }

    @Override
    public void updateBidList(Integer id, BidList bidList) {
        bidList.setBidListId(id);
        bidListRepository.save(bidList);
    }

    @Override
    public void deleteBidListById(Integer id) {
        bidListRepository.deleteById(id);
    }
}
