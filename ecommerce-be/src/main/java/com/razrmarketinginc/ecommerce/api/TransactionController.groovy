package com.razrmarketinginc.ecommerce.api

import com.razrmarketinginc.ecommerce.EcommerceProperties
import com.razrmarketinginc.ecommerce.api.dto.IssueReq
import com.razrmarketinginc.ecommerce.api.dto.RedeemReq
import com.razrmarketinginc.ecommerce.api.dto.ReturnReq
import com.razrmarketinginc.ecommerce.api.dto.TransactionResp
import com.razrmarketinginc.ecommerce.dto.IssuanceTran
import com.razrmarketinginc.ecommerce.dto.RedemptionTran
import com.razrmarketinginc.ecommerce.dto.ReturnTran
import com.razrmarketinginc.ecommerce.entity.Transaction
import com.razrmarketinginc.ecommerce.service.TransactionService
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

import javax.validation.Valid

@RestController
@RequestMapping("/transaction")
class TransactionController {


    @Value('''#{'${logging.level.com.razrmarketinginc.ecommerce}'=='debug'?true:false}''')
    private boolean debug;

    EcommerceProperties ecommerceProperties
    TransactionService transactionService

    TransactionController(
            EcommerceProperties ecommerceProperties,
            TransactionService transactionService
    ){
        this.ecommerceProperties = ecommerceProperties
        this.transactionService = transactionService
    }

    @PostMapping("/issue")
    TransactionResp issueTransaction(@Valid @RequestBody IssueReq req){
        IssuanceTran issuanceTran = transactionService.accept(
                new IssuanceTran(
                    accountId: req.accountId,
                    purchaseNumber: req.purchaseNumber,
                    points: req.points

                ))

        return new TransactionResp(
                id: issuanceTran.id,
                accountId: issuanceTran.accountId,
                balance: issuanceTran.balance
        )
    }

    @PostMapping("/redeem")
    TransactionResp redeemTransaction(@Valid @RequestBody RedeemReq req){
        RedemptionTran redemptionTran = transactionService.accept(
                new RedemptionTran(
                    accountId: req.accountId,
                    goodsId: req.goodsId,
                    orderId: req.orderId,
                    points: req.points
                ))

        return new TransactionResp(
                id: redemptionTran.id,
                accountId: redemptionTran.accountId,
                balance: redemptionTran.balance
        )

    }

    @PostMapping("/return")
    TransactionResp returnTransaction(@Valid @RequestBody ReturnReq req){
        ReturnTran returnTran = transactionService.accept(
                new ReturnTran(
                    accountId: req.accountId,
                    orgTransactionId: req.orgTransactionId
                ))

        return new TransactionResp(
                id: returnTran.id,
                accountId: returnTran.accountId,
                balance: returnTran.balance
        )

    }




}
