package com.nnk.springboot.unitaire;

import com.nnk.springboot.model.RuleName;
import com.nnk.springboot.repositories.RuleNameRepository;
import com.nnk.springboot.services.RuleNameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RuleNameServiceTest {

    @Mock
    private RuleNameRepository ruleNameRepository;

    @InjectMocks
    private RuleNameService ruleNameService;

    private RuleName ruleName;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ruleName = new RuleName();
        ruleName.setId(1);
        ruleName.setName("Test Rule");
        ruleName.setDescription("Description");
        ruleName.setJson("JSON");
        ruleName.setTemplate("Template");
        ruleName.setSqlStr("SQL String");
        ruleName.setSqlPart("SQL Part");
    }

    @Test
    void saveRuleName_ShouldReturnSavedRuleName() {
        when(ruleNameRepository.save(ruleName)).thenReturn(ruleName);

        RuleName savedRuleName = ruleNameService.saveRuleName(ruleName);

        verify(ruleNameRepository, times(1)).save(ruleName);
        assertEquals(ruleName, savedRuleName);
    }

    @Test
    void getAllRuleNames_ShouldReturnListOfRuleNames() {
        List<RuleName> ruleNames = Arrays.asList(ruleName, new RuleName());

        when(ruleNameRepository.findAll()).thenReturn(ruleNames);

        List<RuleName> result = ruleNameService.getAllRuleNames();

        verify(ruleNameRepository, times(1)).findAll();
        assertEquals(2, result.size());
    }

    @Test
    void findRuleNameById_ShouldReturnRuleName() {
        when(ruleNameRepository.findById(1)).thenReturn(Optional.of(ruleName));

        Optional<RuleName> foundRuleName = ruleNameService.findRuleNameById(1);

        verify(ruleNameRepository, times(1)).findById(1);
        assertTrue(foundRuleName.isPresent());
        assertEquals(1, foundRuleName.get().getId());
    }

    @Test
    void updateRuleName_ShouldSetIdAndSave() {
        RuleName updatedRuleName = new RuleName();
        updatedRuleName.setName("Updated Name");

        when(ruleNameRepository.save(any(RuleName.class))).thenReturn(updatedRuleName);

        ruleNameService.updateRuleName(1, updatedRuleName);

        ArgumentCaptor<RuleName> captor = ArgumentCaptor.forClass(RuleName.class);
        verify(ruleNameRepository).save(captor.capture());
        RuleName savedRuleName = captor.getValue();

        assertEquals(1, savedRuleName.getId());
        assertEquals("Updated Name", savedRuleName.getName());
    }

    @Test
    void deleteRuleNameById_ShouldCallDeleteById() {
        doNothing().when(ruleNameRepository).deleteById(1);

        ruleNameService.deleteRuleNameById(1);

        verify(ruleNameRepository, times(1)).deleteById(1);
    }
}
