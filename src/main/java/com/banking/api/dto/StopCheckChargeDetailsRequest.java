package com.banking.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StopCheckChargeDetailsRequest {


    private BigDecimal amount;
    private String waiver;
    private String chgdesc;

}
