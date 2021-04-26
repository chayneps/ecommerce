package com.razrmarketinginc.ecommerce.repository

import com.razrmarketinginc.ecommerce.entity.Transaction
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable


import java.sql.Timestamp

interface TransactionRepository extends AppRepository<Transaction,String>{

    Transaction findTopByAccount_IdOrderByCreatedDesc(String accountId)

    Optional<Transaction> findByIdAndAccount_Id(String id, String AccountId)


    Page<Transaction> findByAccount_IdAndCreatedGreaterThanEqualAndCreatedLessThan(
            String accountId,
            Timestamp startTimestamp,
            Timestamp endTimestamp,
            Pageable pageRequest
    )

    Transaction findTopByAccount_IdAndCreatedLessThanOrderByCreatedDesc(
            String accountId,
            Timestamp endTimestamp
    )

}
