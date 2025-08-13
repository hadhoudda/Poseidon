package com.nnk.springboot.domain;


import jakarta.persistence.*;

@Entity
@Table(name = "curvepoint")
public class CurvePoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // TODO: Map columns in data table CURVEPOINT with corresponding java fields
}
