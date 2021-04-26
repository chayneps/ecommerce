package com.razrmarketinginc.ecommerce.service

import com.razrmarketinginc.ecommerce.EcommerceProperties
import com.razrmarketinginc.ecommerce.dto.IssuanceTran
import com.razrmarketinginc.ecommerce.dto.RedemptionTran
import com.razrmarketinginc.ecommerce.dto.ReturnTran
import com.razrmarketinginc.ecommerce.entity.Account
import com.razrmarketinginc.ecommerce.entity.Transaction
import com.razrmarketinginc.ecommerce.exception.AppError
import com.razrmarketinginc.ecommerce.exception.ApplicationException
import com.razrmarketinginc.ecommerce.repository.TransactionRepository
import com.razrmarketinginc.ecommerce.util.JsonUtil
import com.razrmarketinginc.ecommerce.util.UuidUtil
import groovy.util.logging.Slf4j
import org.apache.commons.lang3.StringUtils
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import javax.persistence.EntityManager
import javax.persistence.OptimisticLockException
import java.sql.Timestamp

@Slf4j
@Service
class TransactionServiceImpl implements TransactionService{

    EcommerceProperties ecommerceProperties

    AccountService accountService

    TransactionRepository transactionRepository

    EntityManager entityManager

    TransactionServiceImpl(
            EcommerceProperties ecommerceProperties,
            AccountService accountService,
            TransactionRepository transactionRepository,
            EntityManager entityManager
    ){
        this.ecommerceProperties = ecommerceProperties
        this.accountService = accountService
        this.transactionRepository = transactionRepository
        this.entityManager = entityManager

    }

    @Override
    @Transactional
    IssuanceTran accept(IssuanceTran issuanceTran) {
        try{
            Account account=null

            if(StringUtils.isBlank(issuanceTran.accountId)){

                account = accountService.create(new Account(
                        id:UuidUtil.randomBase32()
                ))
                issuanceTran.accountId = account.id

            } else {
                account = accountService.findActiveById(issuanceTran.accountId)
            }

            Transaction newTran = new Transaction(
                    id: UuidUtil.randomBase32(),
                    account: account,
                    type: Transaction.ISSUANCE,
                    point: issuanceTran.points,
                    balance: issuanceTran.points,
                    additionalFields: JsonUtil.marshall([
                            (PURCHASE_NUMBER): issuanceTran.purchaseNumber
                    ])

            )

            Transaction formerTransaction = transactionRepository
                    .findTopByAccount_IdOrderByCreatedDesc(issuanceTran.accountId);

            if(formerTransaction!=null){
                newTran.balance = formerTransaction.balance+issuanceTran.points
                formerTransaction.nextTransaction = newTran
            }

            transactionRepository.create(newTran)

            if(formerTransaction!=null)
                transactionRepository.modify(formerTransaction)

            entityManager.flush()


            return issuanceTran.tap{
                id = newTran.id
                balance = newTran.balance
            }
        } catch(OptimisticLockException oe){
            throw new ApplicationException(oe,AppError.TRANSACTION_FAIL)
        }
    }

    @Override
    @Transactional
    RedemptionTran accept(RedemptionTran redemptionTran) {

        try{

            Account account = accountService.findActiveById(redemptionTran.accountId)

            Transaction formerTransaction = transactionRepository
                    .findTopByAccount_IdOrderByCreatedDesc(redemptionTran.accountId);

            if(formerTransaction==null ||
                    formerTransaction.balance<redemptionTran.points)
                throw new ApplicationException(null,null,
                        AppError.POINT_NOT_ENOUGH,
                        [[balance:formerTransaction.balance]])


            Transaction newTran = new Transaction(
                    id: UuidUtil.randomBase32(),
                    account: account,
                    type: Transaction.REDEMPTION,
                    point: redemptionTran.points,
                    balance: formerTransaction.balance - redemptionTran.points,
                    additionalFields: JsonUtil.marshall([
                            (GOODS_ID): redemptionTran.goodsId,
                            (ORDER_ID): redemptionTran.orderId,
                    ])

            )

            formerTransaction.nextTransaction=newTran

            transactionRepository.create(newTran)

            transactionRepository.modify(formerTransaction)

            entityManager.flush()

            return redemptionTran.tap {
                id = newTran.id
                balance = newTran.balance
            }

        } catch(OptimisticLockException oe){
            throw new ApplicationException(oe,AppError.TRANSACTION_FAIL)
        }
    }

    @Override
    @Transactional
    ReturnTran accept(ReturnTran returnTran) {
        try {

            Account account = accountService.findActiveById(returnTran.accountId)

            Transaction rdmTran = transactionRepository.findByIdAndAccount_Id(
                    returnTran.orgTransactionId, returnTran.accountId).orElseThrow(
                    () -> new ApplicationException(AppError.TRAN_NOT_FOUND)
            )

            if (rdmTran.type != Transaction.REDEMPTION)
                throw new ApplicationException(AppError.ONLY_CAN_RETURN_REDEMPTION)

            Map<String, Object> rdmTranAddFields = JsonUtil.unmarshall(
                    rdmTran.additionalFields, Map<String, Object>)

            if (rdmTranAddFields.containsKey(RETURNED_BY_TRAN_ID))
                throw new ApplicationException(
                        null,null,
                        AppError.ORG_TRAN_HAS_BEEN_RETURN,
                        [[(RETURNED_BY_TRAN_ID):rdmTranAddFields[(RETURNED_BY_TRAN_ID)]]])


            Transaction formerTransaction = transactionRepository
                    .findTopByAccount_IdOrderByCreatedDesc(account.id);

            Transaction newTran = new Transaction(
                    id: UuidUtil.randomBase32(),
                    account: account,
                    type: Transaction.RETURN,
                    point: rdmTran.point,
                    balance: formerTransaction.balance + rdmTran.point,
                    additionalFields: JsonUtil.marshall([
                            (ORG_TRANSACTION_ID): rdmTran.id
                    ])
            )


            rdmTran.additionalFields = JsonUtil.marshall(
                    rdmTranAddFields << [(RETURNED_BY_TRAN_ID): newTran.id])

            formerTransaction.nextTransaction = newTran

            transactionRepository.create(newTran)

            transactionRepository.modify(formerTransaction)

            transactionRepository.modify(rdmTran)

            entityManager.flush()

            return returnTran.tap {
                id = newTran.id
                balance = newTran.balance
            }
        } catch(OptimisticLockException oe){
            throw new ApplicationException(oe,AppError.TRANSACTION_FAIL)
        }

    }

    @Override
    Page<Transaction> findByCreated(
                        String accountId,
                        Timestamp startTimestamp,
                        Timestamp endTimestamp,
                        Pageable pageRequest
    ){
        return transactionRepository
            .findByAccount_IdAndCreatedGreaterThanEqualAndCreatedLessThan(
                    accountId,
                    startTimestamp,
                    endTimestamp,
                    pageRequest)
    }

    @Override
    Transaction findLastTransaction(
            String accountId,
            Timestamp endTimestamp
    ){

        return transactionRepository
            .findTopByAccount_IdAndCreatedLessThanOrderByCreatedDesc(
                accountId,
                endTimestamp
            )

    }

}
