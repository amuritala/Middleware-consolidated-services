package com.banking.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateCorporateRequest {

    private String ctype;
    private String name;
    private String addrln1;
    private String addrln3;
    private String addrln2;
    private String addrln4;
    private String country;
    private String sname;
    private String nlty;
    private String ccateg;
    private String fullname;
    private String media;
    private String loc;
    private String gendr;
    private CustomerCorporate custcorp;
}
