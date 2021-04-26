package com.razrmarketinginc.ecommerce.service

import com.razrmarketinginc.ecommerce.dto.AccountDTO
import com.razrmarketinginc.ecommerce.entity.Account

interface AccountService {

    AccountDTO createAccount(AccountDTO accountDTO)

    Account create(Account account)

    AccountDTO getAccount(String id)

    AccountDTO getAccountByAccountNumber(Integer accountNumber)

    AccountDTO modifyAccount(AccountDTO accountDTO)

    boolean deleteAccount(String id)

    Account findActiveById(String id)


}
