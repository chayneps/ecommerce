package com.razrmarketinginc.ecommerce.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
public abstract class RestResponseDTO<T> extends DTO {

    private String Code;
    private String Description;
    private String Message;


    public abstract T getInfo();


}
