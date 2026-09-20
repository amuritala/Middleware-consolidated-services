package com.banking.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Location {

    @JsonProperty("loc_code")
    private String locCode;

    private String description;
}
