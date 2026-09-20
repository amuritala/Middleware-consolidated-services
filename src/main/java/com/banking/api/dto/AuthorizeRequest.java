package com.banking.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuthorizeRequest {

    private String referenceno;
    private String batchno;
    private String branchcode;
}
