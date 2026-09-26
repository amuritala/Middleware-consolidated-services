package com.banking.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonReversalEntriesRequest {

    private String trnrefno;
    private BigDecimal acentrysrno;
    private String eventt;
    private String drcrindd;
    private String trncode;
    private BigDecimal fcyamountt;
    private BigDecimal exchrate;
    private BigDecimal lcyamountt;
    private XMLGregorianCalendar trndt;
    private XMLGregorianCalendar valuedt;
    private String relatedaccount;
    private String relatedreference;
    private String module;
    private String amounttag;

}
