package com.banking.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.datatype.XMLGregorianCalendar;

@Data
@NoArgsConstructor
public class CustomerPersonal {

    private String fstname;
    private String midname;
    private String lstname;
    private XMLGregorianCalendar dob;
    private String gendr;
    private String nationid;
    private String telephno;
    private String lang;
    private String pptno;
    private XMLGregorianCalendar pptissdt;
    private XMLGregorianCalendar pptexpdt;
}
