package com.banking.api.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReversalRequest {

    private String amounttagg;
    private String trnrefno;
    private String eventsrnoo;
    List<CommonReversalEntriesRequest> acvwsAllAcEntriesA ;

}
