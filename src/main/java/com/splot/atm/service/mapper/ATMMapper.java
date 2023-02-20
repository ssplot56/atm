package com.splot.atm.service.mapper;

import com.splot.atm.dto.response.ATMResponseDto;
import com.splot.atm.model.ATM;
import org.springframework.stereotype.Component;

@Component
public class ATMMapper implements ResponseDtoMapper<ATMResponseDto, ATM>{
    @Override
    public ATMResponseDto mapToDto(ATM atm) {
        ATMResponseDto responseDto = new ATMResponseDto();
        responseDto.setId(atm.getId());
        responseDto.setName(atm.getName());
        return responseDto;
    }
}
