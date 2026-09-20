package com.banking.api.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CustomerStatQuery {

    private String customerno;
    private String customeraccno;
    private String branchcode;
    private BigDecimal simpleavgbalance;
    private BigDecimal minbal;
    private BigDecimal maxbal;
    private BigDecimal nodrtransactions;
    private BigDecimal nocrtransactions;
    private BigDecimal dravg;
    private BigDecimal cravg;
    private BigDecimal noretcheques;
    private BigDecimal daysindebit;
    private BigDecimal daysincredit;
    private BigDecimal totbal;
    private BigDecimal nooverdraft;
}
