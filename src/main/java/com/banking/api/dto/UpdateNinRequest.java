package com.banking.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateNinRequest {

    private String customer_no;
    private String unique_id_value;

}
