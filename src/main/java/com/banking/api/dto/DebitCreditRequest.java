package com.banking.api.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DebitCreditRequest {
    private String xref ;
    private String batchno ;
    private String prd ;
    private String branchcode;
    private String dracc;
    private BigDecimal lcyamt;
    private Date txndate ;
    private Date valuedate;
    private String narration;
    private BigDecimal txnamt;
    private String cracc;
    private String crbrn;
}
