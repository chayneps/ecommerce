package com.razrmarketinginc.ecommerce.service

import com.razrmarketinginc.ecommerce.dto.IssuanceTran
import com.razrmarketinginc.ecommerce.dto.RedemptionTran
import com.razrmarketinginc.ecommerce.dto.ReturnTran
import com.razrmarketinginc.ecommerce.entity.Transaction
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

import java.sql.Timestamp

interface TransactionService {

    public static final String PURCHASE_NUMBER = "purchaseNumber"
    public static final String GOODS_ID = "goodsId"
    public static final String ORDER_ID = "orderId"
    public static final String ORG_TRANSACTION_ID = "orgTransactionId"
    public static final String RETURNED_BY_TRAN_ID = "returnedByTranId"

    IssuanceTran accept(IssuanceTran issuanceTran)

    RedemptionTran accept(RedemptionTran redemptionTran)

    ReturnTran accept(ReturnTran returnTran)

    Page<Transaction> findByCreated(
                        String accountId,
                        Timestamp startTimestamp,
                        Timestamp endTimestamp,
                        Pageable pageRequest
    )

    Transaction findLastTransaction(
            String accountId,
            Timestamp endTimestamp
    )

}
