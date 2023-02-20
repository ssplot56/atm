package com.splot.atm.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "atm_denominations")
public class ATMDenomination {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    @JoinColumn(name = "atm_id")
    private ATM atm;
    @ManyToOne
    @JoinColumn(name = "denomination_id")
    private Denomination denomination;
    private int numberOfNotes;
}
