package com.nnk.springboot.unitaire;


import com.nnk.springboot.model.Trade;
import com.nnk.springboot.repositories.TradeRepository;
import com.nnk.springboot.services.TradeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TradeServiceTest {

    @Mock
    private TradeRepository tradeRepository;

    @InjectMocks
    private TradeService tradeService;

    private Trade trade;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        trade = new Trade();
        trade.setTradeId(1);
        trade.setAccount("Account1");
        trade.setType("Type1");
        // Tu peux ajouter plus de champs si besoin
    }

    @Test
    void saveTrade_ShouldReturnSavedTrade() {
        when(tradeRepository.save(trade)).thenReturn(trade);

        Trade savedTrade = tradeService.saveTrade(trade);

        verify(tradeRepository, times(1)).save(trade);
        assertEquals(trade, savedTrade);
    }

    @Test
    void getAllTrades_ShouldReturnListOfTrades() {
        List<Trade> trades = Arrays.asList(trade, new Trade());

        when(tradeRepository.findAll()).thenReturn(trades);

        List<Trade> result = tradeService.getAllTrades();

        verify(tradeRepository, times(1)).findAll();
        assertEquals(2, result.size());
    }

    @Test
    void findTradeById_ShouldReturnTrade() {
        when(tradeRepository.findById(1)).thenReturn(Optional.of(trade));

        Optional<Trade> foundTrade = tradeService.findTradeById(1);

        verify(tradeRepository, times(1)).findById(1);
        assertTrue(foundTrade.isPresent());
        assertEquals(1, foundTrade.get().getTradeId());
    }

    @Test
    void updateTrade_ShouldSetIdAndSaveTrade() {
        Trade tradeToUpdate = new Trade();
        tradeToUpdate.setAccount("NewAccount");
        tradeToUpdate.setType("NewType");

        when(tradeRepository.save(any(Trade.class))).thenReturn(tradeToUpdate);

        tradeService.updateTrade(1, tradeToUpdate);

        ArgumentCaptor<Trade> tradeCaptor = ArgumentCaptor.forClass(Trade.class);
        verify(tradeRepository).save(tradeCaptor.capture());
        Trade savedTrade = tradeCaptor.getValue();

        assertEquals(1, savedTrade.getTradeId());
        assertEquals("NewAccount", savedTrade.getAccount());
        assertEquals("NewType", savedTrade.getType());
    }

    @Test
    void deleteTradeById_ShouldCallDeleteById() {
        doNothing().when(tradeRepository).deleteById(1);

        tradeService.deleteTradeById(1);

        verify(tradeRepository, times(1)).deleteById(1);
    }
}
