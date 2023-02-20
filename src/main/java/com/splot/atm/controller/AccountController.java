package com.splot.atm.controller;

import com.splot.atm.dto.request.DepositRequestDto;
import com.splot.atm.dto.request.TransferRequestDto;
import com.splot.atm.dto.request.WithdrawRequestDto;
import com.splot.atm.dto.response.AccountResponseDto;
import com.splot.atm.model.Account;
import com.splot.atm.service.AccountService;
import com.splot.atm.service.mapper.ResponseDtoMapper;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.security.Principal;

@RestController
@RequestMapping("/atm")
public class AccountController {
    private final AccountService accountService;
    private final ResponseDtoMapper<AccountResponseDto, Account> responseMapper;

    public AccountController(AccountService accountService,
                             ResponseDtoMapper<AccountResponseDto, Account> responseMapper) {
        this.accountService = accountService;
        this.responseMapper = responseMapper;
    }

    @PostMapping("/{atmId}/deposit")
    public AccountResponseDto deposit(@PathVariable Long atmId,
                                      @RequestBody DepositRequestDto requestDto,
                                      Principal principal) {
        String email = principal.getName();
        Account account = accountService.findById(requestDto.getAccountId());
        if (!account.getUser().getEmail().equals(email)) {
            throw new RuntimeException("You are not authorized to deposit to this account");
        }
        return responseMapper.mapToDto(accountService
                .deposit(atmId, requestDto));
    }

    @PostMapping("/{atmId}/withdraw")
    public AccountResponseDto withdraw(@PathVariable Long atmId,
                                       @RequestBody WithdrawRequestDto requestDto,
                                       Principal principal) {
        String email = principal.getName();
        Account account = accountService.findById(requestDto.getAccountId());
        if (!account.getUser().getEmail().equals(email)) {
            throw new RuntimeException("You are not authorized to withdraw to this account");
        }
        return responseMapper.mapToDto(accountService
                .withdraw(atmId, requestDto));
    }

    @PostMapping("/transfer")
    public AccountResponseDto transfer(@RequestBody TransferRequestDto requestDto,
                                       Principal principal) {
        String email = principal.getName();
        Account account = accountService.findById(requestDto.getFromAccountId());
        if (!account.getUser().getEmail().equals(email)) {
            throw new RuntimeException("You are not authorized to transfer from this account");
        }
        return responseMapper.mapToDto(accountService.transfer(requestDto.getFromAccountId(),
                requestDto.getToAccountIt(),
                requestDto.getAmount()));
    }
}
