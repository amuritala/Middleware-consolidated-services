package com.banking.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
public class MultiDeJournalRequest {

    //private String referenceno;
    private String batchno;
    private BigDecimal currno;
    private String templatecode;
    private String valuedate;
    private String branchcode;
    private String ccy;
    private String description;
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
    private List<DetbsJrnlTxnDetail> detbsJrnlTxnDetail;
    private DetbsBatchMaster detbsBatchMaster;
    private DevwsBatchMaster devwsBatchMaster;
    private com.fasterxml.jackson.databind.JsonNode misdetails;
    private List<com.fasterxml.jackson.databind.JsonNode> txnudfdetails;
}
