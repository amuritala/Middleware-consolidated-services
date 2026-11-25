package com.banking.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SmsRequest {
    private String from = "SkyAlert";
    private String to;
    private String content;
    private String refrence;
    private String callbackUrl;
}
