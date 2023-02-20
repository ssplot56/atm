package com.splot.atm.service.impl;

import com.splot.atm.dto.request.DepositRequestDto;
import com.splot.atm.dto.request.WithdrawRequestDto;
import com.splot.atm.model.Account;
import com.splot.atm.repository.AccountRepository;
import com.splot.atm.service.AccountService;
import com.splot.atm.service.AtmService;
import com.splot.atm.service.UserService;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountServiceImpl implements AccountService {
    private static final BigDecimal ONE_HUNDRED = BigDecimal.valueOf(100);
    private static final BigDecimal TWO_HUNDRED = BigDecimal.valueOf(100);
    private static final BigDecimal FIVE_HUNDRED = BigDecimal.valueOf(100);
    private final AccountRepository accountRepository;
    private final AtmService atmService;
    private final UserService userService;

    public AccountServiceImpl(AccountRepository accountRepository,
                              AtmService atmService,
                              UserService userService) {
        this.accountRepository = accountRepository;
        this.atmService = atmService;
        this.userService = userService;
    }

    @Override
    public Account findById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account with id: "
                        + id + " not found"));
    }

    @Override
    @Transactional
    public Account deposit(Long atmId, DepositRequestDto requestDto) {
        atmService.findById(atmId);
        Account account = findById(requestDto.getAccountId());
        BigDecimal amount = ONE_HUNDRED
                .multiply(
                        BigDecimal.valueOf(requestDto.getNumberOfHundredNotes()))
                .add(TWO_HUNDRED.multiply(
                        BigDecimal.valueOf(requestDto.getNumberOfTwoHundredNotes())))
                .add(FIVE_HUNDRED.multiply(
                        BigDecimal.valueOf(requestDto.getNumberOfFiveHundredNotes())));
        account.setBalance(account.getBalance().add(amount));
        atmService.addDeposit(atmId, requestDto);
        return accountRepository.save(account);
    }

    @Override
    @Transactional
    public Account withdraw(Long atmId, WithdrawRequestDto requestDto) {
        if (cantBeObtained(requestDto.getAmount())) {
            throw new RuntimeException("ATM dispenses bills in denominations "
                    + "of 100, 200, 500 only");
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

    @Override
    public Account save(Account account) {
        return accountRepository.save(account);
    }

    @Override
    public Account createNewAccount(Principal principal) {
        String email = principal.getName();
        Account account = new Account();
        account.setBalance(BigDecimal.ZERO);
        account.setNumber(generateCreditCardNumber());
        account.setUser(userService.findUserByEmail(email));
        return accountRepository.save(account);
    }

    @Override
    public List<Account> findAllAccountsByUserEmail(String email) {
        return accountRepository.findAllByUser_Email(email);
    }

    public String generateCreditCardNumber() {
        Random rand = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            sb.append(rand.nextInt(10));
            if ((i + 1) % 4 == 0 && i != 15) {
                sb.append("-");
            }
        }
        return sb.toString();
    }

    private boolean cantBeObtained(BigDecimal amount) {
        return amount.remainder(ONE_HUNDRED).intValue() != 0;
    }
}
