package com.razrmarketinginc.ecommerce.api.dto

import com.razrmarketinginc.ecommerce.dto.DTO

import javax.validation.constraints.Min

class CreateAccountReq extends DTO {
    @Min(1L)
    Integer AccountNumber
}

class CreateAccountResp extends DTO {
    String id
    Integer accountNumber
}

class GetAccountResp extends DTO {
    String id
    Integer accountNumber
    Long created
}

class PutAccountResp extends DTO {
    boolean success
}

class DeleteAccountResp extends DTO {
    boolean success
}



