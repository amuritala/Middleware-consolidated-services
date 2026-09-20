package com.banking.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonAlias;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountResponse {

    @JsonAlias({"fcubsheader", "FCUBSHEADER"})
    private FcubsResponseHeader fcubsheader;

    @JsonAlias({"fcubsbody", "FCUBSBODY"})
    private FcubsBody fcubsbody;
}
