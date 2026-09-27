package com.banking.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AccountStatDetailRequest {

    private String accbrn;
    private String acc;
    private String accountclass;
    private String acccurr;
    private String accdesc;
}
