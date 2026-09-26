package com.banking.api.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DetbsJrnlTxnDetail {

    @JsonAlias("SERIALNO")
    private Integer serialno;
    @JsonAlias("USERREFNO")
    private String userrefno;
    @JsonAlias("DRCR")
    private String drcr;
    @JsonAlias("BRANCHCODE")
    private String branchcode;
    @JsonAlias("ACCORGL")
    private String accorgl;
    @JsonAlias("CCY")
    private String ccy;
    @JsonAlias("AMOUNT")
    private BigDecimal amount;
    @JsonAlias("TXNCODE")
    private String txncode;
    @JsonAlias("INSTRUMENTNO")
    private String instrumentno;
    @JsonAlias("LCYAMOUNT")
    private BigDecimal lcyamount;
    @JsonAlias("ADDLTEXT")
    private String addltext;
    @JsonAlias("ACDESC")
    private String acdesc;
    @JsonAlias("CUSTOMER")
    private String customer;
    @JsonAlias("EXCHRATE")
    private BigDecimal exchrate;
    @JsonAlias("ACCOUNT")
    private String account;
}
