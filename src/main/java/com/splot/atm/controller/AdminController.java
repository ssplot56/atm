package com.splot.atm.controller;

import com.splot.atm.dto.request.DenominationRequestDto;
import com.splot.atm.dto.request.ATMRequestDto;
import com.splot.atm.dto.response.ATMDenominationResponseDto;
import com.splot.atm.dto.response.ATMResponseDto;
import com.splot.atm.model.ATM;
import com.splot.atm.model.ATMDenomination;
import com.splot.atm.service.ATMService;
import com.splot.atm.service.mapper.ResponseDtoMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/atm")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private final ATMService atmService;
    private final ResponseDtoMapper<ATMResponseDto, ATM> atmResponseMapper;
    private final ResponseDtoMapper<ATMDenominationResponseDto,
            ATMDenomination> atmDenominationResponseMapper;

    public AdminController(ATMService atmService,
                           ResponseDtoMapper<ATMResponseDto, ATM> atmResponseMapper,
                           ResponseDtoMapper<ATMDenominationResponseDto, ATMDenomination> atmDenominationResponseMapper) {
        this.atmService = atmService;
        this.atmResponseMapper = atmResponseMapper;
        this.atmDenominationResponseMapper = atmDenominationResponseMapper;
    }

    @PostMapping
    public ATMResponseDto createATM(@RequestBody ATMRequestDto requestDto) {
        return atmResponseMapper.mapToDto(atmService.createATM(requestDto.getName()));
    }

    @PostMapping("/{id}/denomination")
    public ATMDenominationResponseDto addDenomination(@PathVariable Long id,
                                                      @RequestBody DenominationRequestDto requestDto) {
        return atmDenominationResponseMapper.mapToDto(atmService
                .addDenomination(id, requestDto.getValue(), requestDto.getNumberOfNotes()));
    }

    @PostMapping("/{atmId}/denomination/{atmDenominationId}")
    public ATMDenominationResponseDto updateDenomination(@PathVariable Long atmId,
                                                         @PathVariable Long atmDenominationId,
                                                         @RequestBody DenominationRequestDto requestDto) {
        return atmDenominationResponseMapper.mapToDto(atmService
                .updateDenomination(atmId, atmDenominationId, requestDto.getNumberOfNotes()));
    }
}
