package com.banking.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerQueryRequest {

    private String customerno;
    private String customeraccno;
    private String branchcode;
}
