package com.banking.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CustomerCategory {

    @JsonProperty("cust_cat")
    private String custCat;

    @JsonProperty("cust_cat_desc")
    private String custCatDesc;
}
