package com.banking.api.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MultiJrnlBookFull {

    @JsonAlias("REFERENCENO")
    private String referenceno;
    @JsonAlias("BATCHNO")
    private String batchno;
    @JsonAlias("CURRNO")
    private BigDecimal currno;
    @JsonAlias("TEMPLATECODE")
    private String templatecode;
    @JsonAlias("VALUEDATE")
    private String valuedate;
    @JsonAlias("BRANCHCODE")
    private String branchcode;
    @JsonAlias("CCY")
    private String ccy;
    @JsonAlias("TOTALDR")
    private BigDecimal totaldr;
    @JsonAlias("TOTALCR")
    private BigDecimal totalcr;
    @JsonAlias("MAKER")
    private String maker;
    @JsonAlias("MAKDTTIME")
    private String makdttime;
    @JsonAlias("CHECHKERID")
    private String chechkerid;
    @JsonAlias("CHKDTTIME")
    private String chkdttime;
    @JsonAlias("AUTHSTAT")
    private String authstat;
    @JsonAlias("TXNSTAT")
    private String txnstat;
    @JsonAlias("FUNDID")
    private String fundid;
    @JsonAlias("RECNO")
    private BigDecimal recno;
    @JsonAlias("TOTALNO")
    private BigDecimal totalno;
    @JsonAlias("TXNUDFDETAILS")
    private List<JsonNode> txnudfdetails;
    @JsonAlias("DETSBATCHMASTER")
    private DetbsBatchMaster detbsBatchMaster;
    @JsonAlias("DETSJRNLTXNDETAIL")
    private List<DetbsJrnlTxnDetail> detbsJrnlTxnDetail;
    @JsonAlias("DEVWSBATCHMASTER")
    private DevwsBatchMaster devwsBatchMaster;
    @JsonAlias("MISDETAILS")
    private JsonNode misdetails;
}
