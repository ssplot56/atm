package com.splot.atm.controller;

import com.splot.atm.dto.request.DepositRequestDto;
import com.splot.atm.dto.request.TransferRequestDto;
import com.splot.atm.dto.response.AccountResponseDto;
import com.splot.atm.model.Account;
import com.splot.atm.service.AccountService;
import com.splot.atm.service.mapper.ResponseDtoMapper;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.math.BigDecimal;

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

    //TODO change from requestParam to dto
    @PostMapping("/{atmId}/deposit")
    public AccountResponseDto deposit(@PathVariable Long atmId,
                                      @RequestParam Long accountId,
                                      @RequestBody DepositRequestDto requestDto) {
        return responseMapper.mapToDto(accountService
                .deposit(atmId, accountId, requestDto));
    }

    @PostMapping("/{atmId}/withdraw")
    public AccountResponseDto withdraw(@PathVariable Long atmId,
                                       @RequestParam Long accountId,
                                       @RequestParam BigDecimal amount) {
        return responseMapper.mapToDto(accountService
                .withdraw(atmId, accountId, amount));
    }

    @PostMapping("/transfer")
    public AccountResponseDto transfer(@RequestBody TransferRequestDto requestDto) {
        return responseMapper.mapToDto(accountService.transfer(requestDto.getFromAccountId(),
                requestDto.getToAccountIt(),
                requestDto.getAmount()));
    }
}
