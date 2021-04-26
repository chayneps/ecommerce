package com.razrmarketinginc.ecommerce.dto

import com.razrmarketinginc.ecommerce.api.dto.SortOption

import java.sql.Timestamp
import java.time.Instant

class StatementQuery {
    String accountId
    Instant startDateTime
    Instant endDateTime
    SortOption sortedBy
    Integer pageNum
    Integer pageSize
}
