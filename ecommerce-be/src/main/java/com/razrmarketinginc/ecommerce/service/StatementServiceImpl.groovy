package com.razrmarketinginc.ecommerce.service

import com.razrmarketinginc.ecommerce.EcommerceProperties
import com.razrmarketinginc.ecommerce.api.dto.SortOption
import com.razrmarketinginc.ecommerce.api.dto.Statement
import com.razrmarketinginc.ecommerce.dto.IssuanceTran
import com.razrmarketinginc.ecommerce.dto.RedemptionTran
import com.razrmarketinginc.ecommerce.dto.ReturnTran
import com.razrmarketinginc.ecommerce.dto.StatementQuery
import com.razrmarketinginc.ecommerce.dto.TransactionDTO
import com.razrmarketinginc.ecommerce.entity.Account
import com.razrmarketinginc.ecommerce.entity.Transaction
import com.razrmarketinginc.ecommerce.util.JsonUtil
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import java.sql.Timestamp
import java.time.Instant
import java.util.stream.Collectors

@Slf4j
@Service
class StatementServiceImpl implements StatementService {

    @Value('''#{'${logging.level.com.razrmarketinginc.ecommerce}'=='debug'?true:false}''')
    private boolean debug;

    AccountService accountService
    TransactionService transactionService

    EcommerceProperties props

    StatementServiceImpl(
            AccountService accountService,
            TransactionService transactionService,
            EcommerceProperties props
    ){
        this.accountService = accountService
        this.transactionService = transactionService
        this.props = props
    }

    @Override
    @Transactional
    Statement queryStatement(StatementQuery query) {

        Account account = accountService.findActiveById(query.accountId)

        Timestamp startTimestamp = Timestamp.from(query.startDateTime)
        Timestamp endTimestamp = Timestamp.from(query.endDateTime)

        Statement statement = new Statement(
                accountId: account.id,
                accountNumber: account.accountNumber,
                pageNum: query.pageNum,
                pageSize: query.pageSize,
                startEpoch: query.startDateTime.toEpochMilli(),
                endEpoch: query.endDateTime.toEpochMilli()
        )



        Pageable pageRequest = PageRequest.of(
                query.pageNum,
                query.pageSize,
                genSortedBy(query.sortedBy)
        )

        Page<Transaction> result = transactionService.findByCreated(
                account.id,
                startTimestamp,
                endTimestamp,
                pageRequest
        )

        statement.numberOfItems = result.getTotalElements()?.intValue()

        statement.transactions = result.getContent().stream()
                                    .map(t->toTransactionDTO(t))
                                    .collect(Collectors.toList())


        statement.balance = transactionService.findLastTransaction(
                account.id,
                endTimestamp
        )?.balance

        return statement
    }

    TransactionDTO toTransactionDTO(Transaction tran){
        TransactionDTO tDTO

        Map<String,Object> additionalFields = JsonUtil.unmarshall(tran.additionalFields,Map<String,Object>)
        switch(tran.type){
            case Transaction.ISSUANCE:
                tDTO = new IssuanceTran(
                    type: Transaction.ISSUANCE,
                    purchaseNumber: additionalFields?[TransactionService.PURCHASE_NUMBER]

                )
            break;

            case Transaction.REDEMPTION:
                tDTO = new RedemptionTran(
                    type: Transaction.REDEMPTION,
                    orderId: additionalFields?[TransactionService.ORDER_ID],
                    goodsId: additionalFields?[TransactionService.GOODS_ID]
                )
            break
            case Transaction.RETURN:
            default:
                tDTO = new ReturnTran(
                    type: Transaction.RETURN,
                    orgTransactionId: additionalFields?[TransactionService.ORG_TRANSACTION_ID]
                )
        }

        tDTO.id= tran.id
        tDTO.points = tran.point
        tDTO.created = tran.created.getTime()

        return tDTO
    }


    private static Sort genSortedBy(SortOption sortOption){

        switch (sortOption){
            case SortOption.DATE_ASC:
                return Sort.by("created").ascending()
            case SortOption.DATE_DESC:
                return Sort.by("created").descending()
            case SortOption.VALUE_ASC:
                return Sort.by("point").ascending()
            case SortOption.VALUE_DESC:
                return Sort.by("point").descending()
            default:
                return null
        }

    }
}
