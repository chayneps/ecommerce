package com.razrmarketinginc.ecommerce.service

import com.razrmarketinginc.ecommerce.api.dto.Statement
import com.razrmarketinginc.ecommerce.dto.StatementQuery

interface StatementService {

    Statement queryStatement(StatementQuery query)

}
