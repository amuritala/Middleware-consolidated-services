package com.banking.api.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MonthlyStat {

    private String ccy;
    private BigDecimal simpleavgbal;
    private BigDecimal minbal;
    private BigDecimal maxbal;
    private BigDecimal nodrtransactions;
    private BigDecimal nocrtransaction;
    private BigDecimal closingbal;
    private BigDecimal drint;
    private String nsf;
    private BigDecimal chgtr;
    private BigDecimal dbchkrtd;
    private BigDecimal visadb;
    private BigDecimal lastdebit;
    private BigDecimal lastcredit;
    private BigDecimal lastoverdraft;
    private BigDecimal daysindebit;
    private BigDecimal dravgmonthly;
    private BigDecimal daysxod;
    private BigDecimal monthlyavg;
    private BigDecimal nooverdraft;
    private BigDecimal daysincredit;
    private BigDecimal cravgmonthly;
    private String customerno;
    private String custacno;
    private String customername;
    private String branchcode;
    private BigDecimal totbal;
    private BigDecimal dravg;
    private BigDecimal cravg;
    private BigDecimal xodavg;
    private String rundate;
    private BigDecimal noretcheques;
}
