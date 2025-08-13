package com.nnk.springboot.domain;


import jakarta.persistence.*;

@Entity
@Table(name = "trade")
public class Trade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ou AUTO, selon la stratégie
    private Long id;
    // TODO: Map columns in data table TRADE with corresponding java fields
}
