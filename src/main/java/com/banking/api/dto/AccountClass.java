package com.banking.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AccountClass {

    @JsonProperty("account_class")
    private String accountClass;

    private String description;
}
