package com.banking.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class CustomerMisFullType {

    private String misgrp;
    private String cust;
    private String custno;
    private String brncd;
    private String linktogrp;
    private List<CustomerMisChangeLog> custchnglog = new ArrayList<>();
    private List<CustomerMisBalanceTransferLog> baltrnsfrlog = new ArrayList<>();
    private List<CustomerMisCode> customermis = new ArrayList<>();
    private List<CustomerMisCode> compositemis = new ArrayList<>();
    private CustomerMisLogHead custloghead;
}
