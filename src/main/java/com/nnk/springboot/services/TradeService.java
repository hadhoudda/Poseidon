package com.nnk.springboot.services;

import com.nnk.springboot.model.Trade;
import com.nnk.springboot.repositories.TradeRepository;
import com.nnk.springboot.services.contracts.ITradeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class for managing Trade entities.
 */
@Service
public class TradeService implements ITradeService {

    private final TradeRepository tradeRepository;

    /**
     * Constructor with dependency injection.
     *
     * @param tradeRepository repository for Trade entity
     */
    public TradeService(TradeRepository tradeRepository) {
        this.tradeRepository = tradeRepository;
    }

    /**
     * Saves a trade entity.
     *
     * @param trade the trade to save
     * @return the saved trade
     */
    @Override
    public Trade saveTrade(Trade trade) {
        return tradeRepository.save(trade);
    }

    /**
     * Retrieves all trades.
     *
     * @return list of all trades
     */
    @Override
    public List<Trade> getAllTrades() {
        return tradeRepository.findAll();
    }

    /**
     * Finds a trade by its ID.
     *
     * @param id the trade ID
     * @return an Optional containing the trade if found, empty otherwise
     */
    @Override
    public Optional<Trade> findTradeById(Integer id) {
        return tradeRepository.findById(id);
    }

    /**
     * Updates a trade by setting its ID and saving it.
     *
     * @param id    the trade ID to update
     * @param trade the trade data to update with
     */
    @Override
    public void updateTrade(Integer id, Trade trade) {
        trade.setTradeId(id);
        tradeRepository.save(trade);
    }

    /**
     * Deletes a trade by its ID.
     *
     * @param id the trade ID to delete
     */
    @Override
    public void deleteTradeById(Integer id) {
        tradeRepository.deleteById(id);
    }
}
