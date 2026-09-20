package com.banking.api.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ImageSignature {

    private String customernumber;
    private String sigid;
    private String branch;
    private String signname1;
    private String sigtitle;
    private BigDecimal modno;
    private String maker;
    private String makdttime;
    private String checker;
    private String chkdttime;
    private String customername;
    private String txnstat;
    private String repltoacc;
    private String authstat;
    private List<ImageSignatureDetail> svvwsSifsigdetail;
}
