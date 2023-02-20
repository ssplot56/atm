package com.splot.atm.repository;

import com.splot.atm.model.ATM;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ATMRepository extends JpaRepository<ATM, Long> {
}
