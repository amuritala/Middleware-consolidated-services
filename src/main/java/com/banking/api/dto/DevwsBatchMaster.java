package com.banking.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DevwsBatchMaster {

    private String batchnumber;
    private String description;
    private String debit;
    private String credit;
    private String balancing;
}
