package com.splot.atm.repository;

import com.splot.atm.model.Denomination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DenominationRepository extends JpaRepository<Denomination, Long> {
    Denomination findByNominal(int nominal);
}
