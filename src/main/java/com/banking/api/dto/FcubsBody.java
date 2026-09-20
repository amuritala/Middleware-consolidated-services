package com.banking.api.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class FcubsBody {

    private List<ErrorResponse> fcubserrorresp;
    private List<WarningResponse> fcubswarningresp;
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
