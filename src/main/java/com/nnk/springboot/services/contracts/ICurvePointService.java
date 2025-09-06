package com.nnk.springboot.services.contracts;

import com.nnk.springboot.model.CurvePoint;

import java.util.List;
import java.util.Optional;

public interface ICurvePointService {

    CurvePoint saveCurvePoint(CurvePoint curvePoint);

    List<CurvePoint> getAllCurvePoints();

    Optional<CurvePoint> findCurvePointById(Integer id);

    void updateCurvePoint(Integer id, CurvePoint curvePoint);

    void deleteCurvePointById(Integer id);    
}
