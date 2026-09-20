package com.banking.api.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MultiJrnlBookFull {

    private String referenceno;
    private String batchno;
    private BigDecimal currno;
    private String templatecode;
    private String valuedate;
    private String branchcode;
    private String ccy;
    private BigDecimal totaldr;
    private BigDecimal totalcr;
    private String maker;
    private String makdttime;
    private String chechkerid;
    private String chkdttime;
    private String authstat;
    private String txnstat;
    private String fundid;
    private BigDecimal recno;
    private BigDecimal totalno;
}
