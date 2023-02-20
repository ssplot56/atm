package com.splot.atm.service;

import com.splot.atm.dto.request.DepositRequestDto;
import com.splot.atm.dto.request.WithdrawRequestDto;
import com.splot.atm.model.Account;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

public interface AccountService {
    Account save(Account account);

    Account createNewAccount(Principal principal);

    Account findById(Long id);

    Account deposit(Long atmId, DepositRequestDto requestDto);

    Account withdraw(Long atmId, WithdrawRequestDto requestDto);

    Account transfer(Long fromAccountId, Long toAccountIt, BigDecimal amount);

    List<Account> findAllAccountsByUserEmail(String email);
}
