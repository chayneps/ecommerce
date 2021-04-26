package com.razrmarketinginc.ecommerce.repository

import com.razrmarketinginc.ecommerce.entity.Account

interface AccountRepository extends AppRepository<Account,String>{

    Optional<Account> findByIdAndStatus(String id,String status)
    Optional<Account> findByAccountNumberAndStatus(Integer accountNumber,String status)


}
