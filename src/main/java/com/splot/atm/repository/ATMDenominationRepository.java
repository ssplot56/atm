package com.splot.atm.repository;

import com.splot.atm.model.ATMDenomination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ATMDenominationRepository extends JpaRepository<ATMDenomination, Long> {
    List<ATMDenomination> findAllByAtm_Id(Long id);
}
