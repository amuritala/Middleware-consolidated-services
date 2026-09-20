package com.banking.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDetailsRequest {

    @NotBlank(message = "Branch code cannot be empty")
    private String branchcode;

    @NotBlank(message = "Account number cannot be empty")
    private String custacno;
}
