package com.banking.api.dto;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonAlias;

import java.util.List;
import java.util.Map;

@Data
public class FcubsBody {

    @JsonAlias({"fcubserrorresp", "FCUBSERRORRESP"})
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
    private AccountBalanceResult accbalance;
    private CustomerStatQuery cumulativeIO;
    private CustomerStatFull cumulativeFull;
    private AdhocStatementResult custAccStmtAdhocRequest;
    private MultiJrnlBookFull detbsJrnlTxnMasterFull;
    private Map<String, Object> customerFull;
    private AccountStatusMaster accStatMasterFull;
    private Map<String, Object> transactionDetails;
    private ImageSignature svvwsSifsigmasterIO;
    private ImageSignature svvwsSifsigmasterFull;
}
