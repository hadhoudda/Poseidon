package com.nnk.springboot.services.contracts;

import com.nnk.springboot.model.RuleName;

import java.util.List;
import java.util.Optional;

public interface IRuleNameService {

    RuleName saveRuleName(RuleName ruleName);

    List<RuleName> getAllRuleNames();

    Optional<RuleName> findRuleNameById(Integer id);

    void updateRuleName(Integer id, RuleName ruleName);

    void deleteRuleNameById(Integer id);
}