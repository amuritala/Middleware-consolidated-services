package com.banking.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class DetbsJrnlTxnDetail {

    private Integer serialno;
    private String userrefno;
    private String drcr;
    private String branchcode;
    private String accorgl;
    private String ccy;
    private BigDecimal amount;
    private String txncode;
    private String instrumentno;
    private BigDecimal lcyamount;
    private String addltext;
    private String acdesc;
    private String customer;
    private BigDecimal exchrate;
    private String account;
}
