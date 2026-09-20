package com.banking.api.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class AccountBalance {

    private String branchcode;
    private String custacno;
    private String ccy;
    private String trndt;
    private BigDecimal opnbal;
    private BigDecimal curbal;
    private BigDecimal avlbal;
    private BigDecimal uncolamt;
    private BigDecimal avlcr;
    private BigDecimal mtdtovcr;
    private BigDecimal mtdtovdr;
    private BigDecimal acybkdamt;
    private BigDecimal accrdr;
    private BigDecimal accrcr;
    private BigDecimal sublimit;
    private BigDecimal todlimit;
    private String todstdt;
    private String todexpdt;
    private BigDecimal acytankcr;
    private BigDecimal acytankdr;
    private BigDecimal acytankuncol;
    private BigDecimal acyunauthdr;
    private BigDecimal acyunauthtankdr;
    private BigDecimal acyunauthcr;
    private BigDecimal acyunauthtankcr;
    private BigDecimal acyunauthuncol;
    private BigDecimal acyunauthtankuncol;
    private List<IntLiqd> intliqd;
}
