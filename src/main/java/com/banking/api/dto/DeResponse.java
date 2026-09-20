package com.banking.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DeResponse {

    private FcubsResponseHeader fcubsheader;
    private FcubsBody fcubsbody;
}
