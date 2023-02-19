package com.splot.atm.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DepositRequestDto {
    private Long id;
    private int numberOfHundredNotes;
    private int numberOfTwoHundredNotes;
    private int numberOfFiveHundredNotes;
}
