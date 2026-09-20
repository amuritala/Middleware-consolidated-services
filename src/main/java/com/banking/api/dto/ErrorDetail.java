package com.banking.api.dto;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonAlias;

@Data
public class ErrorDetail {

    @JsonAlias({"ecode", "ECODE"})
    private String ecode;
    @JsonAlias({"edesc", "EDESC"})
    private String edesc;
}
