package com.banking.api.dto;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class StatChangeRequest {

    private String refno;
    private String custid;
    private String accountclass;
    private String acccurr;
    private String action;
    private String restrtype;
    private List<JsonNode> accStatDetail;
}
