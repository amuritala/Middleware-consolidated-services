package com.banking.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StopCheckChargesRequest {


    private String referenceno;

    protected List<StopCheckChargeDetailsRequest> chargeDetails;
}
