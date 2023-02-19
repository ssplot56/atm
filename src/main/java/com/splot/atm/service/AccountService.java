package com.splot.atm.service;

import com.splot.atm.dto.request.DepositRequestDto;
import com.splot.atm.model.Account;

import java.math.BigDecimal;

public interface AccountService {
    Account save(Account account);

    Account findById(Long id);

    Account deposit(Long atmId, Long accountId, DepositRequestDto requestDto);

    Account withdraw(Long atmId, Long accountId, BigDecimal amount);

    Account transfer(Long fromAccountId, Long toAccountIt, BigDecimal amount);
}
