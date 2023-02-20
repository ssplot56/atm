package com.splot.atm.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AtmDenominationResponseDto {
    private Long id;
    private Long atmId;
    private Long denominationId;
    private int numberOfNotes;
}
