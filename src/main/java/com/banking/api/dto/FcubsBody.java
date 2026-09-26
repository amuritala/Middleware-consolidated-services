package com.banking.api.dto;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;
import java.util.Map;

@Data
public class FcubsBody {

    private List<ErrorResponse> fcubserrorresp;
    @JsonAlias({"fcubswarningresp", "FCUBSWARNINGRESP"})
    private List<WarningResponse> fcubswarningresp;
    @JsonAlias({"custAccountFull", "CUSTACCOUNTFULL"})
    private Map<String, Object> custAccountFull;
    @JsonAlias({"custAccountIO", "CUSTACCOUNTIO"})
    private Map<String, Object> custAccountIO;
    @JsonAlias({"custDetailsFull", "CUSTDETAILSFULL"})
    private Map<String, Object> custDetailsFull;
    @JsonAlias({"custDetailsIO", "CUSTDETAILSIO"})
    private Map<String, Object> custDetailsIO;
    @JsonAlias({"sttmsCustomerFull", "STTMSCUSTOMERFULL"})
    private Map<String, Object> sttmsCustomerFull;
    @JsonAlias({"sttmsCustomerIO", "STTMSCUSTOMERIO"})
    private Map<String, Object> sttmsCustomerIO;
    @JsonAlias({"mainFull", "MAINFULL"})
    private Map<String, Object> mainFull;
    @JsonAlias({"mainIO", "MAINIO"})
    private Map<String, Object> mainIO;
    @JsonAlias({"amountBlocksFull", "AMOUNTBLOCKSFULL"})
    private Map<String, Object> amountBlocksFull;
    @JsonProperty("amountBlocksIO")
    @JsonAlias("AMOUNTBLOCKSIO")
    private Map<String, Object> amountBlocksIO;
    private AccountBalanceResult accbalance;
    private CustomerStatQuery cumulativeIO;
    private CustomerStatFull cumulativeFull;
    private AdhocStatementResult custAccStmtAdhocRequest;
    private MultiJrnlBookFull detbsJrnlTxnMasterFull;
    @JsonAlias({"detbsJrnlTxnMasterIO", "DETBSJRNLTXNMASTERIO"})
    private Map<String, Object> detbsJrnlTxnMasterIO;
    @JsonAlias({"transactionDetailsFull", "TRANSACTIONDETAILSFULL"})
    private Map<String, Object> transactionDetailsFull;
    @JsonAlias({"RTProductFull", "RTPRODUCTFULL"})
    private Map<String, Object> rtProductFull;
    @JsonAlias({"RTProductIO", "RTPRODUCTIO"})
    private Map<String, Object> rtProductIO;
    private Map<String, Object> customerFull;
    private AccountStatusMaster accStatMasterFull;
    private Map<String, Object> transactionDetails;
    private ImageSignature svvwsSifsigmasterIO;
    private ImageSignature svvwsSifsigmasterFull;

    @JsonSetter("fcubserrorresp")
    public void setFcubserrorresp(JsonNode errors) {
        if (errors == null || errors.isNull()) {
            this.fcubserrorresp = null;
        } else if (errors.isArray()) {
            this.fcubserrorresp = new com.fasterxml.jackson.databind.ObjectMapper()
                    .convertValue(errors, new com.fasterxml.jackson.core.type.TypeReference<>() {});
        } else {
            this.fcubserrorresp = List.of(new com.fasterxml.jackson.databind.ObjectMapper()
                    .convertValue(errors, ErrorResponse.class));
        }
    }

    @JsonSetter("FCUBSERRORRESP")
    public void setUppercaseFcubserrorresp(JsonNode errors) {
        setFcubserrorresp(errors);
    }
}
