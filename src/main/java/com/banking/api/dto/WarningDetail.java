package com.banking.api.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

@Data
public class WarningDetail {

    @JsonAlias({"wcode", "WCODE"})
    private String wcode;
    @JsonAlias({"wdesc", "WDESC"})
    private String wdesc;
}
