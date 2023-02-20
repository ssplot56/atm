package com.splot.atm.controller;

import com.splot.atm.dto.request.AtmRequestDto;
import com.splot.atm.dto.request.DenominationRequestDto;
import com.splot.atm.dto.request.DepositRequestDto;
import com.splot.atm.dto.request.WithdrawRequestDto;
import com.splot.atm.dto.response.AccountResponseDto;
import com.splot.atm.dto.response.AtmDenominationResponseDto;
import com.splot.atm.dto.response.AtmResponseDto;
import com.splot.atm.model.Account;
import com.splot.atm.model.Atm;
import com.splot.atm.model.AtmDenomination;
import com.splot.atm.service.AccountService;
import com.splot.atm.service.AtmService;
import com.splot.atm.service.mapper.ResponseDtoMapper;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/atms")
public class AtmController {
    private final AccountService accountService;
    private final AtmService atmService;
    private final ResponseDtoMapper<AtmResponseDto, Atm> atmResponseMapper;
    private final ResponseDtoMapper<AtmDenominationResponseDto,
            AtmDenomination> atmDenominationResponseMapper;
    private final ResponseDtoMapper<AccountResponseDto, Account> accountResponseMapper;

    public AtmController(AccountService accountService,
                         AtmService atmService,
                         ResponseDtoMapper<AtmResponseDto, Atm> atmResponseMapper,
                         ResponseDtoMapper<AtmDenominationResponseDto,
                                 AtmDenomination> atmDenominationResponseMapper,
                         ResponseDtoMapper<AccountResponseDto, Account> responseAccountMapper) {
        this.accountService = accountService;
        this.atmService = atmService;
        this.atmResponseMapper = atmResponseMapper;
        this.atmDenominationResponseMapper = atmDenominationResponseMapper;
        this.accountResponseMapper = responseAccountMapper;
    }

    @PostMapping("/new-atm")
    @PreAuthorize("hasRole('ADMIN')")
    public AtmResponseDto createAtm(@RequestBody AtmRequestDto requestDto) {
        return atmResponseMapper.mapToDto(atmService.createAtm(requestDto.getName()));
    }

    @PostMapping("/{atmId}/denomination")
    @PreAuthorize("hasRole('ADMIN')")
    public AtmDenominationResponseDto addDenomination(@PathVariable Long atmId,
                                                      @RequestBody DenominationRequestDto
                                                              requestDto) {
        return atmDenominationResponseMapper.mapToDto(atmService
                .addDenomination(atmId, requestDto.getValue(), requestDto.getNumberOfNotes()));
    }

    @PutMapping("/{atmId}/denomination/{atmDenominationId}")
    @PreAuthorize("hasRole('ADMIN')")
    public AtmDenominationResponseDto updateDenomination(@PathVariable Long atmId,
                                                         @PathVariable Long atmDenominationId,
                                                         @RequestBody DenominationRequestDto
                                                                     requestDto) {
        return atmDenominationResponseMapper.mapToDto(atmService
                .updateDenomination(atmId, atmDenominationId, requestDto.getNumberOfNotes()));
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
        return accountResponseMapper.mapToDto(accountService
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
        return accountResponseMapper.mapToDto(accountService
                .withdraw(atmId, requestDto));
    }

    @GetMapping
    public List<AtmResponseDto> findAll() {
        return atmService.findAll().stream()
                .map(atmResponseMapper::mapToDto)
                .collect(Collectors.toList());
    }
}
