package com.banking.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class CreateTellerRequest {

    private String txttxndesc;
    private String txtdescription;
    private String txnbranch;
    private String productcode;
    private String referenceno;
    private String txnccy;
    private BigDecimal instramtf;
    private String transactionaccount;
    private String instrumentno;
    private String clgbankcode;
    private String offsetccy;
    private BigDecimal offsetamount;
    private String offsetaccount;
    private BigDecimal amtsttlacy;
    private BigDecimal exrate;
    private String batchnumber;
    private String offsetaccbranch;
    private BigDecimal exchrate;
}
