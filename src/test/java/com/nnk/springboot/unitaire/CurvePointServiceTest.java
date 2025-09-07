package com.nnk.springboot.unitaire;

import com.nnk.springboot.model.CurvePoint;
import com.nnk.springboot.repositories.CurvePointRepository;
import com.nnk.springboot.services.CurvePointService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CurvePointServiceTest {

    @Mock
    private CurvePointRepository curvePointRepository;

    @InjectMocks
    private CurvePointService curvePointService;

    private CurvePoint curvePoint;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        curvePoint = new CurvePoint();
        curvePoint.setCurveId(1);
        curvePoint.setTerm(10.0);
        curvePoint.setValue(20.0);
    }

    @Test
    void saveCurvePoint_ShouldReturnSavedCurvePoint() {
        when(curvePointRepository.save(curvePoint)).thenReturn(curvePoint);

        CurvePoint savedCurvePoint = curvePointService.saveCurvePoint(curvePoint);

        verify(curvePointRepository, times(1)).save(curvePoint);
        assertEquals(curvePoint, savedCurvePoint);
    }

    @Test
    void getAllCurvePoints_ShouldReturnListOfCurvePoints() {
        List<CurvePoint> curvePoints = Arrays.asList(curvePoint, new CurvePoint());

        when(curvePointRepository.findAll()).thenReturn(curvePoints);

        List<CurvePoint> result = curvePointService.getAllCurvePoints();

        verify(curvePointRepository, times(1)).findAll();
        assertEquals(2, result.size());
    }

    @Test
    void findCurvePointById_ShouldReturnCurvePoint() {
        when(curvePointRepository.findById(1)).thenReturn(Optional.of(curvePoint));

        Optional<CurvePoint> foundCurvePoint = curvePointService.findCurvePointById(1);

        verify(curvePointRepository, times(1)).findById(1);
        assertTrue(foundCurvePoint.isPresent());
        assertEquals(1, foundCurvePoint.get().getCurveId());
    }

    @Test
    void updateCurvePoint_ShouldSetIdAndSave() {
        CurvePoint updatedCurvePoint = new CurvePoint();
        updatedCurvePoint.setTerm(15.0);
        updatedCurvePoint.setValue(25.0);

        when(curvePointRepository.save(any(CurvePoint.class))).thenReturn(updatedCurvePoint);

        curvePointService.updateCurvePoint(1, updatedCurvePoint);

        ArgumentCaptor<CurvePoint> captor = ArgumentCaptor.forClass(CurvePoint.class);
        verify(curvePointRepository).save(captor.capture());
        CurvePoint savedCurvePoint = captor.getValue();

        assertEquals(1, savedCurvePoint.getCurveId());
        assertEquals(15.0, savedCurvePoint.getTerm());
        assertEquals(25.0, savedCurvePoint.getValue());
    }

    @Test
    void deleteCurvePointById_ShouldCallDeleteById() {
        doNothing().when(curvePointRepository).deleteById(1);

        curvePointService.deleteCurvePointById(1);

        verify(curvePointRepository, times(1)).deleteById(1);
    }
}
