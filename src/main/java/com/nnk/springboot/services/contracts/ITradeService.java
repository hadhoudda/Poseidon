package com.nnk.springboot.services.contracts;

import com.nnk.springboot.model.Trade;

import java.util.List;
import java.util.Optional;

public interface ITradeService {

    Trade saveTrade(Trade trade);

    List<Trade> getAllTrades();

    Optional<Trade> findTradeById(Integer id);

    void updateTrade(Integer id, Trade trade);

    void deleteTradeById(Integer id);
}
