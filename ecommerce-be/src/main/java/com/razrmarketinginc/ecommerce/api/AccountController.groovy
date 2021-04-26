package com.razrmarketinginc.ecommerce.api

import com.razrmarketinginc.ecommerce.EcommerceProperties
import com.razrmarketinginc.ecommerce.api.dto.CreateAccountReq
import com.razrmarketinginc.ecommerce.api.dto.CreateAccountResp
import com.razrmarketinginc.ecommerce.api.dto.DeleteAccountResp
import com.razrmarketinginc.ecommerce.api.dto.GetAccountResp
import com.razrmarketinginc.ecommerce.api.dto.PutAccountResp
import com.razrmarketinginc.ecommerce.dto.AccountDTO
import com.razrmarketinginc.ecommerce.service.AccountService
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Value
import org.springframework.util.MimeType
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

import javax.validation.Valid

@Slf4j
@RestController
@RequestMapping("/account")
class AccountController {

    @Value('''#{'${logging.level.com.razrmarketinginc.ecommerce}'=='debug'?true:false}''')
    private boolean debug;

    EcommerceProperties ecommerceProperties

    AccountService accountService

    AccountController(
            EcommerceProperties ecommerceProperties,
            AccountService accountService
    ){
        this.ecommerceProperties = ecommerceProperties
        this.accountService = accountService
    }


    @PostMapping('/register')
    CreateAccountResp createAccount(@Valid @RequestBody CreateAccountReq req) {
        AccountDTO accountDTO = accountService.createAccount(
                new AccountDTO(accountNumber:req.accountNumber))

        return new CreateAccountResp(
                        id:accountDTO.id,
                        accountNumber: accountDTO.accountNumber)
    }

    @GetMapping('{id}')
    GetAccountResp getAccount(@PathVariable String id){
        AccountDTO accountDTO = accountService.getAccount(id)
        return new GetAccountResp(
                id: accountDTO.id,
                accountNumber: accountDTO.accountNumber,
                created: accountDTO.created.getTime()
        )
    }

    @GetMapping('/accountnumber/{accountNumber}')
    GetAccountResp getAccountByAccountNumber(@PathVariable Integer accountNumber){
        AccountDTO accountDTO = accountService.getAccountByAccountNumber(accountNumber)
        return new GetAccountResp(
                id: accountDTO.id,
                accountNumber: accountDTO.accountNumber,
                created: accountDTO.created.getTime()
        )
    }

    @PutMapping('{id}')
    PutAccountResp putAccount(@PathVariable String id,
                              @Valid @RequestBody CreateAccountReq req){


        AccountDTO accountDTO = accountService.modifyAccount(new AccountDTO(
                id: id,
                accountNumber: req.accountNumber
        ))

        return new PutAccountResp(
                success: true
        )

    }

    @DeleteMapping('{id}')
    DeleteAccountResp deleteAccount(@PathVariable String id){
        return new DeleteAccountResp(
                success: accountService.deleteAccount(id)
        )
    }

}
