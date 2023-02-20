package com.splot.atm.repository;

import com.splot.atm.model.AtmDenomination;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AtmDenominationRepository extends JpaRepository<AtmDenomination, Long> {
    List<AtmDenomination> findAllByAtm_Id(Long id);
}
