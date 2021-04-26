package com.razrmarketinginc.ecommerce.api.dto

import com.fasterxml.jackson.annotation.JsonInclude
import com.razrmarketinginc.ecommerce.dto.DTO
import com.razrmarketinginc.ecommerce.dto.TransactionDTO

@JsonInclude(JsonInclude.Include.NON_NULL)
class Statement extends DTO{

    String accountId
    Integer accountNumber
    Integer balance

    Long startEpoch
    Long endEpoch
    List<TransactionDTO> transactions

    Integer pageNum
    Integer pageSize
    Integer numberOfItems




}

enum SortOption{
    DATE_ASC,
    DATE_DESC,
    VALUE_ASC,
    VALUE_DESC
}

