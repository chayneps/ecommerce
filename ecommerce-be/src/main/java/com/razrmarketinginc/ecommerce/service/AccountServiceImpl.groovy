package com.razrmarketinginc.ecommerce.service

import com.razrmarketinginc.ecommerce.EcommerceApplication
import com.razrmarketinginc.ecommerce.EcommerceProperties
import com.razrmarketinginc.ecommerce.dto.AccountDTO
import com.razrmarketinginc.ecommerce.dto.IssuanceTran
import com.razrmarketinginc.ecommerce.entity.Account
import com.razrmarketinginc.ecommerce.entity.ENTITY
import com.razrmarketinginc.ecommerce.exception.AppError
import com.razrmarketinginc.ecommerce.exception.ApplicationException
import com.razrmarketinginc.ecommerce.repository.AccountRepository
import com.razrmarketinginc.ecommerce.util.UuidUtil
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Lazy
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

import javax.persistence.EntityManager
import javax.persistence.PersistenceException
import javax.validation.ConstraintViolationException

@Slf4j
@Service
class AccountServiceImpl implements AccountService{

    @Value('''#{'${logging.level.com.razrmarketinginc.ecommerce}'=='debug'?true:false}''')
    private boolean debug;

    EcommerceProperties ecommerceProperties
    AccountRepository accountRepository

    EntityManager entityManager


    AccountServiceImpl(
            EcommerceProperties ecommerceProperties,
            AccountRepository accountRepository,
            EntityManager entityManager
    ){
        this.ecommerceProperties = ecommerceProperties
        this.accountRepository = accountRepository
        this.entityManager = entityManager

    }

    @Override
    @Transactional
    AccountDTO createAccount(AccountDTO accountDTO){

        Account account;

        try {
            account = this.create(new Account(
                    id: UuidUtil.randomBase32(),
                    accountNumber: accountDTO.accountNumber,
            ))
        } catch(ConstraintViolationException ce){
            throw new ApplicationException(ce,AppError.ACCOUNT_NUMBER_DUPLICATED)

        }


        return new AccountDTO(
            id: account.id,
            accountNumber: account.accountNumber,
            created: account.created
        )

    }

    @Override
    Account create(Account account){
        return accountRepository.create(account)

    }

    @Override
    @Transactional(readOnly = true)
    AccountDTO getAccount(String id) {

        Account account = this.findActiveById(id)

        return new AccountDTO(
                id: account.id,
                accountNumber: account.accountNumber,
                created: account.created
        )
    }

    @Override
    @Transactional(readOnly = true)
    AccountDTO getAccountByAccountNumber(Integer accountNumber) {
        Account account = accountRepository.findByAccountNumberAndStatus(accountNumber,ENTITY.ACTIVE)
                                .orElseThrow(
                ()-> new ApplicationException(AppError.ACCOUNT_NOT_FOUND)
        )

        return new AccountDTO(
                id: account.id,
                accountNumber: account.accountNumber,
                created: account.created
        )
    }

    @Override
    @Transactional
    AccountDTO modifyAccount(AccountDTO accountDTO) {

        Account account

        try{
            account = this.findActiveById(accountDTO.id)

            account = accountRepository.modify(account.tap{
                it.accountNumber = accountDTO.accountNumber
            })

            entityManager.flush()

        } catch(PersistenceException pe){
            throw new ApplicationException(pe,AppError.ACCOUNT_NUMBER_DUPLICATED)

        }

        return new AccountDTO(
                id: account.id,
                accountNumber: account.accountNumber,
                created: account.created
        )


    }

    @Override
    @Transactional
    boolean deleteAccount(String id) {
        accountRepository.del(id)
        return true;
    }

    @Override
    Account findActiveById(String id){
        return accountRepository.findByIdAndStatus(id, ENTITY.ACTIVE).orElseThrow(
                ()-> new ApplicationException(AppError.ACCOUNT_NOT_FOUND)
        )
    }
}
