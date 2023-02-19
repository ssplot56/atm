package com.splot.atm.dto.response;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountResponseDto {
    private Long id;
    private int number;
    private BigDecimal balance;
}
