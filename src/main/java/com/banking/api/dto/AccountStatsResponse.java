package com.banking.api.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AccountStatsResponse {

    @JsonProperty("fcubsheader")
    @JsonAlias("FCUBSHEADER")
    private FcubsResponseHeader fcubsheader;

    @JsonProperty("fcubsbody")
    @JsonAlias("FCUBSBODY")
    private FcubsBody fcubsbody;
}
