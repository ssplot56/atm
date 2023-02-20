package com.splot.atm.controller;

import com.splot.atm.dto.request.TransferRequestDto;
import com.splot.atm.dto.response.AccountResponseDto;
import com.splot.atm.model.Account;
import com.splot.atm.service.AccountService;
import com.splot.atm.service.mapper.ResponseDtoMapper;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/accounts")
public class AccountController {
    private final AccountService accountService;
    private final ResponseDtoMapper<AccountResponseDto, Account> responseMapper;

    public AccountController(AccountService accountService,
                             ResponseDtoMapper<AccountResponseDto, Account> responseMapper) {
        this.accountService = accountService;
        this.responseMapper = responseMapper;
    }

    @PostMapping("/new-account")
    public AccountResponseDto createNewAccount(Principal principal) {
        return responseMapper.mapToDto(accountService.createNewAccount(principal));
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

    @GetMapping
    public List<AccountResponseDto> findAllUserAccounts(Principal principal) {
        String email = principal.getName();
        return accountService.findAllAccountsByUserEmail(email).stream()
                .map(responseMapper::mapToDto)
                .collect(Collectors.toList());
    }
}
