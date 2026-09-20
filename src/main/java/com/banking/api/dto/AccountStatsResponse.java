package com.banking.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AccountStatsResponse {

    private FcubsResponseHeader fcubsheader;
    private FcubsBody fcubsbody;
}
