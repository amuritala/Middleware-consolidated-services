package com.banking.api.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RtellerResponse {

    @JsonProperty("fcubsheader")
    @JsonAlias("FCUBSHEADER")
    private FcubsResponseHeader fcubsheader;

    @JsonProperty("fcubsbody")
    @JsonAlias("FCUBSBODY")
    private FcubsBody fcubsbody;
}
