package com.razrmarketinginc.ecommerce.dto

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
class TransactionDTO extends DTO {
    String id

    String accountId

    String type

    Integer points

    Integer balance

    Long created

}

@JsonInclude(JsonInclude.Include.NON_NULL)
class IssuanceTran extends TransactionDTO {
    String purchaseNumber
}

@JsonInclude(JsonInclude.Include.NON_NULL)
class RedemptionTran extends TransactionDTO {

    String orderId

    String goodsId
}

@JsonInclude(JsonInclude.Include.NON_NULL)
class ReturnTran extends TransactionDTO {

    String orgTransactionId
}

