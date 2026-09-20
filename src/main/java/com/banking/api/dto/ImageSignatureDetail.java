package com.banking.api.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ImageSignatureDetail {

    private BigDecimal seqspecnumber;
    private String imagename;
    private String imagetype;
    private String imagetext;
}
