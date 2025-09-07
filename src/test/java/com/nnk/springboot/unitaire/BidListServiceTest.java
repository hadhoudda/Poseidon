package com.nnk.springboot.unitaire;

import com.nnk.springboot.model.BidList;
import com.nnk.springboot.repositories.BidListRepository;
import com.nnk.springboot.services.BidListService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BidListServiceTest {

    @Mock
    private BidListRepository bidListRepository;

    @InjectMocks
    private BidListService bidListService;

    private BidList bidList;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        bidList = new BidList();
        bidList.setBidListId(1);
        bidList.setAccount("Account1");
        bidList.setType("Type1");
        bidList.setBidQuantity(100.0);
    }

    @Test
    void saveBidList_ShouldReturnSavedBidList() {
        when(bidListRepository.save(bidList)).thenReturn(bidList);

        BidList savedBidList = bidListService.saveBidList(bidList);

        verify(bidListRepository, times(1)).save(bidList);
        assertEquals(bidList, savedBidList);
    }

    @Test
    void getAllBidLists_ShouldReturnListOfBidLists() {
        List<BidList> bidLists = Arrays.asList(bidList, new BidList());

        when(bidListRepository.findAll()).thenReturn(bidLists);

        List<BidList> result = bidListService.getAllBidLists();

        verify(bidListRepository, times(1)).findAll();
        assertEquals(2, result.size());
    }

    @Test
    void findBidListById_ShouldReturnBidList() {
        when(bidListRepository.findById(1)).thenReturn(Optional.of(bidList));

        Optional<BidList> foundBidList = bidListService.findBidListById(1);

        verify(bidListRepository, times(1)).findById(1);
        assertTrue(foundBidList.isPresent());
        assertEquals(1, foundBidList.get().getBidListId());
    }

    @Test
    void updateBidList_ShouldSetIdAndSave() {
        BidList updatedBidList = new BidList();
        updatedBidList.setAccount("AccountUpdated");
        updatedBidList.setType("TypeUpdated");
        updatedBidList.setBidQuantity(200.0);

        when(bidListRepository.save(any(BidList.class))).thenReturn(updatedBidList);

        bidListService.updateBidList(1, updatedBidList);

        ArgumentCaptor<BidList> captor = ArgumentCaptor.forClass(BidList.class);
        verify(bidListRepository).save(captor.capture());
        BidList savedBidList = captor.getValue();

        assertEquals(1, savedBidList.getBidListId());
        assertEquals("AccountUpdated", savedBidList.getAccount());
        assertEquals("TypeUpdated", savedBidList.getType());
        assertEquals(200.0, savedBidList.getBidQuantity());
    }

    @Test
    void deleteBidListById_ShouldCallDeleteById() {
        doNothing().when(bidListRepository).deleteById(1);

        bidListService.deleteBidListById(1);

        verify(bidListRepository, times(1)).deleteById(1);
    }
}
