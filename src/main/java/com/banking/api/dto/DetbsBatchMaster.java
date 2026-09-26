package com.banking.api.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DetbsBatchMaster {

    @JsonAlias("BATCHNO")
    private String batchno;
    @JsonAlias("DESCRIPTION")
    private String description;
    @JsonAlias("DEBIT")
    private BigDecimal debit;
    @JsonAlias("CREDIT")
    private BigDecimal credit;
    @JsonAlias("DRENTTOTAL")
    private BigDecimal drenttotal;
    @JsonAlias("CRENTTOTAL")
    private BigDecimal crenttotal;
}
