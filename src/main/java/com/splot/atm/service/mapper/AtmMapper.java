package com.splot.atm.service.mapper;

import com.splot.atm.dto.response.AtmResponseDto;
import com.splot.atm.model.Atm;
import org.springframework.stereotype.Component;

@Component
public class AtmMapper implements ResponseDtoMapper<AtmResponseDto, Atm> {
    @Override
    public AtmResponseDto mapToDto(Atm atm) {
        AtmResponseDto responseDto = new AtmResponseDto();
        responseDto.setId(atm.getId());
        responseDto.setName(atm.getName());
        return responseDto;
    }
}
