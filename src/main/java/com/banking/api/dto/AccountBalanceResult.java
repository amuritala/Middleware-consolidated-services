package com.banking.api.dto;

import lombok.Data;

import java.util.List;

@Data
public class AccountBalanceResult {

    private List<AccountBalance> accbal;
}
