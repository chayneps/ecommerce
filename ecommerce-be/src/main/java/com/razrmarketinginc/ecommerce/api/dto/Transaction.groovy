package com.razrmarketinginc.ecommerce.api.dto

import com.razrmarketinginc.ecommerce.dto.DTO

import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank


class IssueReq extends DTO{

    String accountId

    @NotBlank
    String purchaseNumber

    @Min(1L)
    Integer points

}

class RedeemReq extends DTO {

    @NotBlank
    String accountId

    @NotBlank
    String goodsId

    @NotBlank
    String orderId

    @Min(1L)
    Integer points

}

class ReturnReq extends DTO {

    @NotBlank
    String accountId

    @NotBlank
    String orgTransactionId

}

class TransactionResp extends DTO {
    String id
    String accountId
    Integer balance
}
