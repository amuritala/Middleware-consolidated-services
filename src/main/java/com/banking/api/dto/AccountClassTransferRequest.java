package com.banking.api.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountClassTransferRequest {


    private String chqbook;

    private String passbook;

    private String atm;

    private BigDecimal offlinelimit;

    private String acstmttype2;

    private BigDecimal acstmtday2;


    private String acstmttype3;

    private BigDecimal acstmtday3;

    private String acstmtcycle3;

    private String trackreceivable;

    private String regdapp;

    private String regdper;

    private String refreq;

    private String lodgebkfac;

    private String autoreorderchkr;

    private BigDecimal autoreorderchklvl;

    private BigDecimal autoreorderchklvs;

    private String autostatchange;

    private String allwbkperentry;

    private String exclsamedayrvrtrnsfmstmt;

    private String autoprovreqd;

    private String expcateg;

    private String provccytype;

    private String dormprm;

    private String sweepmode;

    private BigDecimal minreqbal;

    private String consreqd;

    private String acc;    // required

    private String accls; // required

    private String oldacclass;


    private XMLGregorianCalendar effectivedate;

    private String acstmttype;

    private BigDecimal acstmtday;

    private String acstmtcycle;

    private String crgl;

    private String drgl;

    private String crcbline;

    private String drcbline;

    private String crholine;

    private String drholine;

    private String remarks;

    private String processedstat;

    private String brn;



    private String ccy;

    private String atmcustacno;

    private BigDecimal atmdlyamtlmt;

    private BigDecimal atmdlylmtcnt;

    private String checkname1;

    private String checkname2;

    private String accstat;

    private String directbanking;

    private String maker;

    private String makerstamp;

    private String checker;

    private String checkerstamp;



    private String txnstat;

    private String authstat;
}
