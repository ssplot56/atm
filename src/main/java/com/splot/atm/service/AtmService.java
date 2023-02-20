package com.splot.atm.service;

import com.splot.atm.dto.request.DepositRequestDto;
import com.splot.atm.model.Atm;
import com.splot.atm.model.AtmDenomination;
import java.math.BigDecimal;
import java.util.List;

public interface AtmService {
    Atm save(Atm atm);

    Atm findById(Long id);

    List<Atm> findAll();

    Atm createAtm(String name);

    AtmDenomination addDenomination(Long id, int value, int numberOfNotes);

    AtmDenomination updateDenomination(Long atmId, Long atmDenominationId, int numberOfNotes);

    List<AtmDenomination> addDeposit(Long atmId, DepositRequestDto deposit);

    List<AtmDenomination> withdraw(Long atmId, BigDecimal amount);
}
