package com.banking.api.dto;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class JnrMasterFullTemplate {

    private String templatecode;
    private String description;
    private List<JsonNode> detmsJrnlTmplDetail;
}
