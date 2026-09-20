package com.banking.api.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

import java.util.List;

@Data
public class WarningResponse {

    @JsonAlias({"warning", "WARNING"})
    private List<WarningDetail> warning;
}
