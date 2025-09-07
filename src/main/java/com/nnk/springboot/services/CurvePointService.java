package com.nnk.springboot.services;

import com.nnk.springboot.model.CurvePoint;
import com.nnk.springboot.repositories.CurvePointRepository;
import com.nnk.springboot.services.contracts.ICurvePointService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service implementation for managing CurvePoint entities.
 */
@Service
public class CurvePointService implements ICurvePointService {

    private final CurvePointRepository curvePointRepository;

    /**
     * Constructor for CurvePointService.
     *
     * @param curvePointRepository the repository used for CurvePoint entity operations
     */
    public CurvePointService(CurvePointRepository curvePointRepository) {
        this.curvePointRepository = curvePointRepository;
    }

    /**
     * Save a CurvePoint entity.
     *
     * @param curvePoint the CurvePoint entity to save
     * @return the saved CurvePoint entity
     */
    @Override
    public CurvePoint saveCurvePoint(CurvePoint curvePoint) {
        return curvePointRepository.save(curvePoint);
    }

    /**
     * Retrieve all CurvePoint entities.
     *
     * @return a list of all CurvePoint entities
     */
    @Override
    public List<CurvePoint> getAllCurvePoints() {
        return curvePointRepository.findAll();
    }

    /**
     * Find a CurvePoint entity by its id.
     *
     * @param id the id of the CurvePoint entity
     * @return an Optional containing the found CurvePoint or empty if not found
     */
    @Override
    public Optional<CurvePoint> findCurvePointById(Integer id) {
        return curvePointRepository.findById(id);
    }

    /**
     * Update a CurvePoint entity by id.
     *
     * @param id         the id of the CurvePoint to update
     * @param curvePoint the CurvePoint entity containing updated data
     */
    @Override
    public void updateCurvePoint(Integer id, CurvePoint curvePoint) {
        curvePoint.setCurveId(id);
        curvePointRepository.save(curvePoint);
    }

    /**
     * Delete a CurvePoint entity by id.
     *
     * @param id the id of the CurvePoint to delete
     */
    @Override
    public void deleteCurvePointById(Integer id) {
        curvePointRepository.deleteById(id);
    }
}
