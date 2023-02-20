package com.splot.atm.service.mapper;

import com.splot.atm.dto.response.AtmDenominationResponseDto;
import com.splot.atm.model.AtmDenomination;
import org.springframework.stereotype.Component;

@Component
public class AtmDenominationMapper
        implements ResponseDtoMapper<AtmDenominationResponseDto, AtmDenomination> {
    @Override
    public AtmDenominationResponseDto mapToDto(AtmDenomination atmDenomination) {
        AtmDenominationResponseDto responseDto = new AtmDenominationResponseDto();
        responseDto.setId(atmDenomination.getId());
        responseDto.setAtmId(atmDenomination.getAtm().getId());
        responseDto.setDenominationId(atmDenomination.getDenomination().getId());
        responseDto.setNumberOfNotes(atmDenomination.getNumberOfNotes());
        return responseDto;
    }
}
