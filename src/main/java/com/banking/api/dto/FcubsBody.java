package com.banking.api.dto;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

@Data
public class FcubsBody {

    private static final ObjectMapper RESPONSE_MAPPER = new ObjectMapper();

    private List<ErrorResponse> fcubserrorresp;
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
    @JsonAlias({"stvwAccountSumaryFull", "STVWACCOUNTSUMARYFULL"})
    private Map<String, Object> stvwAccountSumaryFull;
    @JsonAlias({"stvwAccountSumaryIO", "STVWACCOUNTSUMARYIO"})
    private Map<String, Object> stvwAccountSumaryIO;
    @JsonAlias({"mainFull", "MAINFULL"})
    private Map<String, Object> mainFull;
    @JsonAlias({"mainIO", "MAINIO"})
    private Map<String, Object> mainIO;
    @JsonAlias({"amountBlocksFull", "AMOUNTBLOCKSFULL"})
    private Map<String, Object> amountBlocksFull;
    @JsonProperty("amountBlocksIO")
    @JsonAlias("AMOUNTBLOCKSIO")
    private Map<String, Object> amountBlocksIO;
    @JsonAlias({"ACCBalance", "ACCBALANCE"})
    private Map<String, Object> accBalance;
    private AccountBalanceResult accbalance;
    private CustomerStatQuery cumulativeIO;
    private CustomerStatFull cumulativeFull;
    private AdhocStatementResult custAccStmtAdhocRequest;
    private MultiJrnlBookFull detbsJrnlTxnMasterFull;
    @JsonAlias({"detbsJrnlTxnMasterIO", "DETBSJRNLTXNMASTERIO"})
    private Map<String, Object> detbsJrnlTxnMasterIO;
    @JsonAlias({"accDetailsFull", "ACCDETAILSFULL"})
    private Map<String, Object> accDetailsFull;
    @JsonAlias({"accDetailsIO", "ACCDETAILSIO"})
    private Map<String, Object> accDetailsIO;
    @JsonAlias({"acvwAcdaudtrFull", "ACVWACDAUDTRFULL"})
    private Map<String, Object> acvwAcdaudtrFull;
    @JsonAlias({"acvwAcdaudtrIO", "ACVWACDAUDTRIO"})
    private Map<String, Object> acvwAcdaudtrIO;
    @JsonAlias({"transactionDetailsFull", "TRANSACTIONDETAILSFULL"})
    private Map<String, Object> transactionDetailsFull;
    @JsonAlias({"RTProductFull", "RTPRODUCTFULL"})
    private Map<String, Object> rtProductFull;
    @JsonAlias({"RTProductIO", "RTPRODUCTIO"})
    private Map<String, Object> rtProductIO;
    private Map<String, Object> customerFull;
    @JsonAlias("ACCSTATMASTERFULL")
    private AccountStatusMaster accStatMasterFull;
    private Map<String, Object> transactionDetails;
    private ImageSignature svvwsSifsigmasterIO;
    private ImageSignature svvwsSifsigmasterFull;

    @JsonSetter("fcubswarningresp")
    public void setFcubswarningresp(JsonNode warnings) {
        if (warnings == null || warnings.isNull()) {
            this.fcubswarningresp = null;
        } else if (warnings.isArray()) {
            this.fcubswarningresp = RESPONSE_MAPPER.convertValue(
                    warnings, new TypeReference<>() {});
        } else {
            this.fcubswarningresp = List.of(RESPONSE_MAPPER.convertValue(warnings, WarningResponse.class));
        }
    }

    @JsonSetter("FCUBSWARNINGRESP")
    public void setUppercaseFcubswarningresp(JsonNode warnings) {
        setFcubswarningresp(warnings);
    }

    @JsonSetter("fcubserrorresp")
    public void setFcubserrorresp(JsonNode errors) {
        if (errors == null || errors.isNull()) {
            this.fcubserrorresp = null;
        } else if (errors.isArray()) {
            this.fcubserrorresp = RESPONSE_MAPPER.convertValue(errors, new TypeReference<>() {});
        } else {
            this.fcubserrorresp = List.of(RESPONSE_MAPPER.convertValue(errors, ErrorResponse.class));
        }
    }

    @JsonSetter("FCUBSERRORRESP")
    public void setUppercaseFcubserrorresp(JsonNode errors) {
        setFcubserrorresp(errors);
    }
}
