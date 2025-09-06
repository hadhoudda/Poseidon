package com.nnk.springboot.services;


import com.nnk.springboot.model.Trade;
import com.nnk.springboot.repositories.TradeRepository;
import com.nnk.springboot.services.contracts.ITradeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TradeService implements ITradeService {

    private final TradeRepository tradeRepository;

    public TradeService(TradeRepository tradeRepository) {
        this.tradeRepository = tradeRepository;
    }


    @Override
    public Trade saveTrade(Trade trade) {
        return tradeRepository.save(trade);
    }

    @Override
    public List<Trade> getAllTrades() {
        return tradeRepository.findAll();
    }

    @Override
    public Optional<Trade> findTradeById(Integer id) {
        return tradeRepository.findById(id);
    }

    @Override
    public void updateTrade(Integer id, Trade trade) {
        trade.setTradeId(id);
        tradeRepository.save(trade);
    }

    @Override
    public void deleteTradeById(Integer id) {
        tradeRepository.deleteById(id);
    }
}
