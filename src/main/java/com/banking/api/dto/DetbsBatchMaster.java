package com.banking.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class DetbsBatchMaster {

    private String batchno;
    private String description;
    private BigDecimal debit;
    private BigDecimal credit;
    private BigDecimal drenttotal;
    private BigDecimal crenttotal;
}
