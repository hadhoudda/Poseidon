package com.nnk.springboot.services;

import com.nnk.springboot.model.RuleName;
import com.nnk.springboot.repositories.RuleNameRepository;
import com.nnk.springboot.services.contracts.IRuleNameService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service implementation for managing RuleName entities.
 */
@Service
public class RuleNameService implements IRuleNameService {

    private final RuleNameRepository ruleNameRepository;

    /**
     * Constructor for RuleNameService.
     *
     * @param ruleNameRepository the repository used for RuleName entity operations
     */
    public RuleNameService(RuleNameRepository ruleNameRepository) {
        this.ruleNameRepository = ruleNameRepository;
    }

    /**
     * Save a RuleName entity.
     *
     * @param ruleName the RuleName entity to save
     * @return the saved RuleName entity
     */
    @Override
    public RuleName saveRuleName(RuleName ruleName) {
        return ruleNameRepository.save(ruleName);
    }

    /**
     * Retrieve all RuleName entities.
     *
     * @return a list of all RuleName entities
     */
    @Override
    public List<RuleName> getAllRuleNames() {
        return ruleNameRepository.findAll();
    }

    /**
     * Find a RuleName entity by its id.
     *
     * @param id the id of the RuleName entity
     * @return an Optional containing the found RuleName or empty if not found
     */
    @Override
    public Optional<RuleName> findRuleNameById(Integer id) {
        return ruleNameRepository.findById(id);
    }

    /**
     * Update a RuleName entity by id.
     *
     * @param id       the id of the RuleName to update
     * @param ruleName the RuleName entity containing updated data
     */
    @Override
    public void updateRuleName(Integer id, RuleName ruleName) {
        ruleName.setId(id);
        ruleNameRepository.save(ruleName);
    }

    /**
     * Delete a RuleName entity by id.
     *
     * @param id the id of the RuleName to delete
     */
    @Override
    public void deleteRuleNameById(Integer id) {
        ruleNameRepository.deleteById(id);
    }
}
