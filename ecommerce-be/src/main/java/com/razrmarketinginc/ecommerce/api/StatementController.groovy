package com.razrmarketinginc.ecommerce.api

import com.razrmarketinginc.ecommerce.EcommerceProperties
import com.razrmarketinginc.ecommerce.api.dto.SortOption
import com.razrmarketinginc.ecommerce.api.dto.Statement
import com.razrmarketinginc.ecommerce.dto.StatementQuery
import com.razrmarketinginc.ecommerce.service.AccountService
import com.razrmarketinginc.ecommerce.service.StatementService
import com.razrmarketinginc.ecommerce.service.TransactionService
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

import java.sql.Timestamp
import java.time.Instant

@Slf4j
@RestController
@RequestMapping("/statement")
class StatementController {

    @Value('''#{'${logging.level.com.razrmarketinginc.ecommerce}'=='debug'?true:false}''')
    private boolean debug

    StatementService statementService

    EcommerceProperties props

    StatementController(
            StatementService statementService,
            EcommerceProperties props
    ){
        this.statementService = statementService
        this.props=props

    }

    @GetMapping("{accountId}")
    Statement getStatement(
            @PathVariable String accountId,
            @RequestParam(required = true) Long startEpoch,
            @RequestParam(required = false) Long endEpoch,
            @RequestParam(defaultValue="DATE_ASC",required = false)
                    SortOption sortedBy,
            @RequestParam(defaultValue = "0",required=false)
                    Integer pageNum
    ){

        Statement statement = statementService.queryStatement(new StatementQuery(
                accountId: accountId,
                startDateTime: Instant.ofEpochMilli((long)startEpoch),
                endDateTime: endEpoch==null?Instant.now()
                                    :Instant.ofEpochMilli((long)endEpoch),
                sortedBy: sortedBy,
                pageNum: pageNum,
                pageSize: props.pageSize
        ))

        return statement

    }



}
