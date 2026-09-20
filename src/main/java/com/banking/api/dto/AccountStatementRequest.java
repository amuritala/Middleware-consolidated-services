package com.banking.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountStatementRequest {

    private String xref;
    private String acc;
    private String brn;
    private String frmdt;
    private String todt;
    private String stmttype;
    private String balancetype;
    private String applychg;
}
