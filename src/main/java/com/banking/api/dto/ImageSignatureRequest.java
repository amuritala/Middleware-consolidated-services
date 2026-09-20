package com.banking.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ImageSignatureRequest {

    private String customernumber;
    private String sigid;
}
