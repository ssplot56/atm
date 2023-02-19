package com.splot.atm.service;

import com.splot.atm.dto.request.DepositRequestDto;
import com.splot.atm.model.ATM;
import com.splot.atm.model.ATMDenomination;
import java.math.BigDecimal;
import java.util.List;

public interface ATMService {
    ATM findById(Long id);
    ATM createATM(String name);

    ATMDenomination addDenomination(Long id, int value, int numberOfNotes);

    ATMDenomination updateDenomination(Long atmId, Long atmDenominationId, int numberOfNotes);

    List<ATMDenomination> addDeposit(Long atmId, DepositRequestDto deposit);

    List<ATMDenomination> withdraw(Long atmId, BigDecimal amount);
}
