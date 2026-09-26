package com.banking.api.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DevwsBatchMaster {

    @JsonAlias("BATCHNUMBER")
    private String batchnumber;
    @JsonAlias("DESCRIPTION")
    private String description;
    @JsonAlias("DEBIT")
    private String debit;
    @JsonAlias("CREDIT")
    private String credit;
    @JsonAlias("BALANCING")
    private String balancing;
}
