package com.nnk.springboot.domain;

//import org.springframework.beans.factory.annotation.Required;

import jakarta.persistence.*;

@Entity
@Table(name = "bidlist")
public class BidList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ou AUTO, selon la stratégie
    private Long id;
    // TODO: Map columns in data table BIDLIST with corresponding java fields
}
