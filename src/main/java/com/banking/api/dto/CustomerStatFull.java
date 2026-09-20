package com.banking.api.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CustomerStatFull {

    private String customerno;
    private String customeraccno;
    private String currency;
    private String customername;
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
    private BigDecimal dravgmonthly;
    private BigDecimal cravgmonthly;
    private BigDecimal monthlyavg;
    private List<MonthlyStat> monthly;
}
