package com.nnk.springboot.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "rulename")
public class RuleName {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ou AUTO, selon la stratégie
    private Long id;
    // TODO: Map columns in data table RULENAME with corresponding java fields
}
