package com.banking.api.dto;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonAlias;

import java.util.List;

@Data
public class ErrorResponse {

    @JsonAlias({"error", "ERROR"})
    private List<ErrorDetail> error;
}
