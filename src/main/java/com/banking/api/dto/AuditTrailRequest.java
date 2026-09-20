package com.banking.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuditTrailRequest {

    private String branchcode;
    private String custacno;
    private String trnfromdt;
    private String trntodt;
    private String action;
    private String chequeno;
}
