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
