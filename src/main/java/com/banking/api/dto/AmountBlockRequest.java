package com.banking.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import javax.xml.datatype.XMLGregorianCalendar;

@Data
@NoArgsConstructor
public class AmountBlockRequest {

    private String rem;
    private String acc;
    private String ablktype;
    private BigDecimal amt;
    private String referenceno;
    private XMLGregorianCalendar expdate;
    private XMLGregorianCalendar effdate;
    private String amtblkno;
    private String holddesc;
    private String branch;
}
