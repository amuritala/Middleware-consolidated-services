package com.banking.api.dto;

import lombok.Data;

import java.util.List;

@Data
public class ErrorResponse {

    private List<ErrorDetail> error;
}
