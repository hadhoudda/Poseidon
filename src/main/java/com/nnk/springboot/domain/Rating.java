package com.nnk.springboot.domain;


import jakarta.persistence.*;

@Entity
@Table(name = "rating")
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ou AUTO, selon la stratégie
    private Long id;
    // TODO: Map columns in data table RATING with corresponding java fields
}
