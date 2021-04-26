package com.razrmarketinginc.ecommerce.api;

import com.razrmarketinginc.ecommerce.exception.ApplicationException;
import com.razrmarketinginc.ecommerce.exception.ServerErrorException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;




import javax.annotation.PostConstruct;
import javax.validation.ConstraintViolationException;

@Slf4j
@RestControllerAdvice
public class RestExceptionHandlers {

    @Value("#{'${logging.level.com.razrmarketinginc.ecommerce}'=='debug'?true:false}")
    private boolean debug;


    @PostConstruct
    private void init(){
        log.debug("debug={}",debug);
    }

    private String self="[ecommerce]";

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorRestResponse>
    handleApplicationException(ApplicationException ae) {
        if(ae.getError()!=null) {
            log.info(ae.getError().getErrorCode().toString()+" - "+ae.getError().getErrorDescription());
            log.debug(ExceptionUtils.getStackTrace(ae));
        }
        return new ResponseEntity<ErrorRestResponse>(
                    new ErrorRestResponse(ae,debug,self),
                    HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(ServerErrorException.class)
    public ResponseEntity<ErrorRestResponse> handleServerErrorException(ServerErrorException see) {
        if(see.getError()!=null) {
            log.info(see.getError().getErrorCode().toString() + "-" + see.getError().getErrorDescription());
            log.debug(ExceptionUtils.getStackTrace(see));
        }
        return new ResponseEntity<ErrorRestResponse>(
                    new ErrorRestResponse(see,debug,self),
                    HttpStatus.INTERNAL_SERVER_ERROR);
    }



    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorRestResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException me,WebRequest request){

        return new ResponseEntity<ErrorRestResponse>(
                        new ErrorRestResponse(me,debug,self),
                        HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorRestResponse> handleConstraintViolationException(ConstraintViolationException ce,
                                                                                WebRequest req) {

        return new ResponseEntity<ErrorRestResponse>(
                        new ErrorRestResponse(ce,debug,self),
                        HttpStatus.BAD_REQUEST);
    }



    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorRestResponse> handleAllException(Exception e) {
        log.error(ExceptionUtils.getStackTrace(e));
        return new ResponseEntity<ErrorRestResponse>(
                new ErrorRestResponse(e,debug,self),
                HttpStatus.INTERNAL_SERVER_ERROR);

    }



}

