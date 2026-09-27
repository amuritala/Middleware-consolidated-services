package com.banking.api.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

@Data
public class AdhocStatementResult {

    @JsonAlias("XREF")
    private String xref;
    @JsonAlias("DCN")
    private String dcn;
}
