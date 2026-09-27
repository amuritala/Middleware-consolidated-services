package com.banking.api.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ImageSignatureDetail {

    @JsonAlias("SEQSPECNUMBER")
    private BigDecimal seqspecnumber;
    @JsonAlias("IMAGENAME")
    private String imagename;
    @JsonAlias("IMAGETYPE")
    private String imagetype;
    @JsonAlias("IMAGETEXT")
    private String imagetext;
}
