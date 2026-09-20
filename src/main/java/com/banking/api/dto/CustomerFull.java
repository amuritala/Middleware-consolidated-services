package com.banking.api.dto;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

@Data
public class CustomerFull {

    private JsonNode customer;
}
