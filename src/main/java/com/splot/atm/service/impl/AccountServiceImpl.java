package com.splot.atm.service.impl;

import com.splot.atm.dto.request.DepositRequestDto;
import com.splot.atm.dto.request.WithdrawRequestDto;
import com.splot.atm.model.Account;
import com.splot.atm.repository.AccountRepository;
import com.splot.atm.service.ATMService;
import com.splot.atm.service.AccountService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.Arrays;

@Service
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final ATMService atmService;
    private static final BigDecimal ONE_HUNDRED = BigDecimal.valueOf(100);
    private static final BigDecimal TWO_HUNDRED = BigDecimal.valueOf(100);
    private static final BigDecimal FIVE_HUNDRED = BigDecimal.valueOf(100);

    public AccountServiceImpl(AccountRepository accountRepository,
                              ATMService atmService) {
        this.accountRepository = accountRepository;
        this.atmService = atmService;
    }

    @Override
    public Account save(Account account) {
        return accountRepository.save(account);
    }

    @Override
    public Account findById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));
    }

    @Override
    @Transactional
    public Account deposit(Long atmId, DepositRequestDto requestDto) {
        atmService.findById(atmId);
        Account account = findById(requestDto.getAccountId());
        BigDecimal amount = ONE_HUNDRED
                .multiply(BigDecimal.valueOf(requestDto.getNumberOfHundredNotes()))
                .add(TWO_HUNDRED.multiply(BigDecimal.valueOf(requestDto.getNumberOfTwoHundredNotes())))
                .add(FIVE_HUNDRED.multiply(BigDecimal.valueOf(requestDto.getNumberOfFiveHundredNotes())));
        account.setBalance(account.getBalance().add(amount));
        atmService.addDeposit(atmId, requestDto);
        return accountRepository.save(account);
    }

    @Override
    @Transactional
    public Account withdraw(Long atmId, WithdrawRequestDto requestDto) {
        if (cantBeObtained(requestDto.getAmount())) {
            throw new RuntimeException("ATM dispenses bills in denominations of 100, 200, 500 only");
        }
        Account account = findById(requestDto.getAccountId());
        atmService.findById(atmId);
        BigDecimal newBalance = account.getBalance().subtract(requestDto.getAmount());
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Insufficient balance");
        }
        atmService.withdraw(atmId, requestDto.getAmount());
        account.setBalance(newBalance);
        return accountRepository.save(account);
    }

    @Override
    @Transactional
    public Account transfer(Long fromAccountId, Long toAccountId, BigDecimal amount) {
        Account fromAccount = findById(fromAccountId);
        Account toAccount = findById(toAccountId);
        BigDecimal newSourceBalance = fromAccount.getBalance().subtract(amount);

        if (newSourceBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Insufficient balance");
        }
        fromAccount.setBalance(newSourceBalance);
        toAccount.setBalance(toAccount.getBalance().add(amount));
        accountRepository.saveAll(Arrays.asList(fromAccount, toAccount));
        return fromAccount;
    }

    private boolean cantBeObtained(BigDecimal amount) {
        return amount.remainder(BigDecimal.valueOf(100)).intValue() != 0;
    }
}
