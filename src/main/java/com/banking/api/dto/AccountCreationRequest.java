package com.banking.api.dto;


import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor
@NoArgsConstructor

public class AccountCreationRequest {

    @NotNull(message = "Branch Code cannot be Empty")
    private String brn;

    @NotNull(message = "Account number cannot be Empty")
    private String acc ;


    @NotNull(message = "Customer number cannot be Empty")
    private String custno ;

    @NotNull(message = "Currency cannot be Empty")
    private String ccy ;

    @NotNull(message = "Account Class cannot be Empty")
    private String accls ;



}
