package com.banking.api.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NinResponse {
    private String unique_id_value;
    private String customer_no ;
    private String customer_name1;
    private String  address_line1;
    private String address_line2;
    private String telephone;

}

