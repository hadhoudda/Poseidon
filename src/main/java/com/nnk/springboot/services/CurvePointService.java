package com.nnk.springboot.services;

import com.nnk.springboot.model.CurvePoint;
import com.nnk.springboot.repositories.CurvePointRepository;
import com.nnk.springboot.services.contracts.ICurvePointService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CurvePointService implements ICurvePointService {

    private final CurvePointRepository curvePointRepository;

    public CurvePointService(CurvePointRepository curvePointRepository) {
        this.curvePointRepository = curvePointRepository;
    }

    @Override
    public CurvePoint saveCurvePoint(CurvePoint curvePoint) {
        return curvePointRepository.save(curvePoint);
    }

    @Override
    public List<CurvePoint> getAllCurvePoints() {
        return curvePointRepository.findAll();
    }

    @Override
    public Optional<CurvePoint> findCurvePointById(Integer id) {
        return curvePointRepository.findById(id);
    }

    @Override
    public void updateCurvePoint(Integer id, CurvePoint curvePoint) {
        curvePoint.setCurveId(id);
        curvePointRepository.save(curvePoint);
    }

    @Override
    public void deleteCurvePointById(Integer id) {

        curvePointRepository.deleteById(id);
    }
}
