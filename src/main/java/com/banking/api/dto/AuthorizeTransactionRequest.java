package com.banking.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuthorizeTransactionRequest {

    private String xref;
    private String prd;
    private String brn;
    private String fccref;
}
