package com.banking.api.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ImageSignature {

    @JsonAlias("CUSTOMERNUMBER")
    private String customernumber;
    @JsonAlias("SIGID")
    private String sigid;
    @JsonAlias("BRANCH")
    private String branch;
    @JsonAlias("SIGNNAME1")
    private String signname1;
    @JsonAlias("SIGTITLE")
    private String sigtitle;
    @JsonAlias("MODNO")
    private BigDecimal modno;
    @JsonAlias("MAKER")
    private String maker;
    @JsonAlias("MAKDTTIME")
    private String makdttime;
    @JsonAlias("CHECKER")
    private String checker;
    @JsonAlias("CHKDTTIME")
    private String chkdttime;
    @JsonAlias("CUSTOMERNAME")
    private String customername;
    @JsonAlias("TXNSTAT")
    private String txnstat;
    @JsonAlias("REPLTOACC")
    private String repltoacc;
    @JsonAlias("AUTHSTAT")
    private String authstat;
    @JsonAlias("SVVWSSIFSIGDETAIL")
    private List<ImageSignatureDetail> svvwsSifsigdetail;
}
