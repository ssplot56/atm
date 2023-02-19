package com.splot.atm.service.mapper;

import com.splot.atm.dto.response.ATMDenominationResponseDto;
import com.splot.atm.model.ATMDenomination;
import org.springframework.stereotype.Component;

@Component
public class ATMDenominationMapper
        implements ResponseDtoMapper<ATMDenominationResponseDto, ATMDenomination> {
    @Override
    public ATMDenominationResponseDto mapToDto(ATMDenomination atmDenomination) {
        ATMDenominationResponseDto responseDto = new ATMDenominationResponseDto();
        responseDto.setId(atmDenomination.getId());
        responseDto.setAtmId(atmDenomination.getAtm().getId());
        responseDto.setDenominationId(atmDenomination.getDenomination().getId());
        responseDto.setNumberOfNotes(atmDenomination.getNumberOfNotes());
        return responseDto;
    }
}
