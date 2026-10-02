package com.banking.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChequeBookRequest {

    @JsonProperty("accountbranch")
    private String accountBranch;

    @JsonProperty("account")
    private String account;

    @JsonProperty("firstchequenumber")
    private String firstChequeNumber;

    @JsonProperty("chequeleaves")
    private Integer chequeLeaves;

    @JsonProperty("orderdetails")
    private String orderDetails;

    @JsonProperty("deliveryadd1")
    private String deliveryAddress1;

    @JsonProperty("applychg")
    private String applyCharge;

    @JsonProperty("cavwsChequeStatus")
    private List<ChequeStatus> cavwsChequeStatus;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ChequeStatus {

        @JsonProperty("chqbookno")
        private String chequeBookNumber;

        @JsonProperty("chqno")
        private String chequeNumber;

        @JsonProperty("status")
        private String status;
    }
}
