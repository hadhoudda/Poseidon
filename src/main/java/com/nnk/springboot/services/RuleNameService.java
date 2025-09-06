package com.nnk.springboot.services;

import com.nnk.springboot.model.RuleName;
import com.nnk.springboot.repositories.RuleNameRepository;
import com.nnk.springboot.services.contracts.IRuleNameService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RuleNameService implements IRuleNameService {

    private final RuleNameRepository ruleNameRepository;

    public RuleNameService(RuleNameRepository ruleNameRepository) {
        this.ruleNameRepository = ruleNameRepository;
    }

    @Override
    public RuleName saveRuleName(RuleName ruleName) {
        return ruleNameRepository.save(ruleName);
    }

    @Override
    public List<RuleName> getAllRuleNames() {
        return ruleNameRepository.findAll();
    }

    @Override
    public Optional<RuleName> findRuleNameById(Integer id) {
        return ruleNameRepository.findById(id);
    }

    @Override
    public void updateRuleName(Integer id, RuleName ruleName) {
        ruleName.setId(id);
        ruleNameRepository.save(ruleName);

    }

    @Override
    public void deleteRuleNameById(Integer id) {
        ruleNameRepository.deleteById(id);

    }
}
