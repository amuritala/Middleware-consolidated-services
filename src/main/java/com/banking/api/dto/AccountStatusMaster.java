package com.banking.api.dto;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class AccountStatusMaster {

    private String refno;
    private String custid;
    private String accountclass;
    private String acccurr;
    private String action;
    private String restrtype;
    private String customername;
    private String maker;
    private String makerstamp;
    private String checker;
    private String checkerstamp;
    private BigDecimal modno;
    private String txnstat;
    private String authstat;
    private List<JsonNode> accStatDetail;
}
