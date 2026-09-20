package com.banking.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountResponse {

    private FcubsResponseHeader fcubsheader;
    private FcubsBody fcubsbody;
}
