package com.splot.atm.service.mapper;

import com.splot.atm.dto.response.AccountResponseDto;
import com.splot.atm.model.Account;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper implements ResponseDtoMapper<AccountResponseDto, Account> {
    @Override
    public AccountResponseDto mapToDto(Account account) {
        AccountResponseDto responseDto = new AccountResponseDto();
        responseDto.setId(account.getId());
        responseDto.setNumber(account.getNumber());
        responseDto.setBalance(account.getBalance());
        return responseDto;
    }
}
