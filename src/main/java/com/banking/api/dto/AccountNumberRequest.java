package com.banking.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountNumberRequest {

    @NotBlank(message = "Account number cannot be empty")
    private String custacno;
    @NotBlank(message = "Account Branch cannot be empty")
    private String branchCode;

}
