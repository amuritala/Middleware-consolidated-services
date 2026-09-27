package com.banking.api.dto;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class AccountStatusMaster {

    @JsonAlias("REFNO")
    private String refno;
    @JsonAlias("CUSTID")
    private String custid;
    @JsonAlias("ACCOUNTCLASS")
    private String accountclass;
    @JsonAlias("ACCCURR")
    private String acccurr;
    @JsonAlias("ACTION")
    private String action;
    @JsonAlias("RESTRTYPE")
    private String restrtype;
    @JsonAlias("CUSTOMERNAME")
    private String customername;
    @JsonAlias("MAKER")
    private String maker;
    @JsonAlias("MAKERSTAMP")
    private String makerstamp;
    @JsonAlias("CHECKER")
    private String checker;
    @JsonAlias("CHECKERSTAMP")
    private String checkerstamp;
    @JsonAlias("MODNO")
    private BigDecimal modno;
    @JsonAlias("TXNSTAT")
    private String txnstat;
    @JsonAlias("AUTHSTAT")
    private String authstat;
    @JsonAlias("accStatDetail")
    private List<JsonNode> accStatDetail;
}
