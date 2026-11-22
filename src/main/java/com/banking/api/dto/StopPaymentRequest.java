package com.banking.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StopPaymentRequest {

    private String accnumber;
    private String stoppaymentno;
    private String stoppaymenttype;
    private String startcheckno;
    private String endcheckno;
    private BigDecimal amount;
    private XMLGregorianCalendar effectivedate;
    private XMLGregorianCalendar expirydate;
    private String confirmed;
    private String reason;
    private String advftreqdflg;
    private String applycharge;

   private StopCheckChargesRequest chargeMain;

    //protected CsconevtCreateIOType eventMaster;

   /// protected List<UDFDETAILSType2> udfdetails;
}
