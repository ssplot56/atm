package com.splot.atm.dto.request;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WithdrawRequestDto {
    private Long accountId;
    private BigDecimal amount;
}
