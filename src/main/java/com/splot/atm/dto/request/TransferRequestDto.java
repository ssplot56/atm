package com.splot.atm.dto.request;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransferRequestDto {
    private Long fromAccountId;
    private Long toAccountIt;
    private BigDecimal amount;
}
