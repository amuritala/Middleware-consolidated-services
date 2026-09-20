package com.banking.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.datatype.XMLGregorianCalendar;

@Data
@NoArgsConstructor
public class CustomerMisChangeLog {

    private String miscls;
    private XMLGregorianCalendar txndt;
    private String oldmiscd;
    private String nwmiscd;
}
