package com.nnk.springboot.services.contracts;

import com.nnk.springboot.model.BidList;

import java.util.List;
import java.util.Optional;

public interface IBidListService {

    BidList saveBidList(BidList bidList);

    List<BidList> getAllBidLists();

    Optional<BidList> findBidListById(Integer id);

    void updateBidList(Integer id, BidList bidList);

    void deleteBidListById(Integer id);
}
