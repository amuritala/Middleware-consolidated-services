package com.banking.api.dto;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
public class MultiDeJournalRequest {

    private String referenceno;
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
    private List<JsonNode> detbsJrnlTxnDetail;
    private JsonNode detbsBatchMaster;
    private JsonNode devwsBatchMaster;
    private JsonNode misdetails;
    private List<JsonNode> txnudfdetails;
}
