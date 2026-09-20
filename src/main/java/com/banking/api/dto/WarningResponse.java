package com.banking.api.dto;

import lombok.Data;

import java.util.List;

@Data
public class WarningResponse {

    private List<WarningDetail> warning;
}
