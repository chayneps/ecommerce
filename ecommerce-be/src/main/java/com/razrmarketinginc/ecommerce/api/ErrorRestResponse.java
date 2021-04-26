package com.razrmarketinginc.ecommerce.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.razrmarketinginc.ecommerce.dto.RestResponseDTO;
import com.razrmarketinginc.ecommerce.exception.ApplicationException;
import com.razrmarketinginc.ecommerce.exception.ServerErrorException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;


import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.nio.file.AccessDeniedException;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"code","description","message"})
public class ErrorRestResponse extends RestResponseDTO<List<Object>> {
    @JsonIgnore
    public static final String GENERIC_ERROR_CODE = "44001";
    @JsonIgnore
    public static final String VALIDATION_ERROR_CODE= "44002";
    @JsonIgnore
    public static final String METHOD_ARGUMENT_ERROR_CODE= "44003";

    @JsonIgnore
    public static final String ACCESS_DENIED_ERROR_CODE="44404";

    private static String message(String self,String message){
        return StringUtils.isNotBlank(message)? self+message:null;
    }

    private List<Object> info;

    private ErrorRestResponse cause;

    private void attachInfo(Exception e, boolean debug){
        if(info==null)
            info= new LinkedList<>();
        if((e instanceof ApplicationException)
                && ((ApplicationException)e).getInfo()!=null
                && ((ApplicationException)e).getInfo().size()>0 ){
            info.addAll(((ApplicationException)e).getInfo());
        }
    }


    public ErrorRestResponse(){

    }

    public ErrorRestResponse(ApplicationException ae, boolean debug, String self){
        if(ae.getError()!=null) {
            super.setCode(ae.getError().getErrorCode().toString());
            super.setDescription(ae.getError().getErrorDescription());
        } else {
            super.setCode(ae.getCauseCode());
            super.setDescription(ae.getCauseDescription());
        }
        super.setMessage(message(self,ae.getMessage()));
        attachInfo(ae,debug);
    }

    public ErrorRestResponse(ServerErrorException ae, boolean debug, String self){
        if(ae.getError()!=null) {
            super.setCode(ae.getError().getErrorCode().toString());
            super.setDescription(ae.getError().getErrorDescription());
        } else {
            super.setCode(ae.getCauseCode());
            super.setDescription(ae.getCauseDescription());
        }
        super.setMessage(message(self,ae.getMessage()));
        attachInfo(ae,debug);
    }

    public ErrorRestResponse(Exception e, boolean debug, String self){
        super.setCode(GENERIC_ERROR_CODE);
        super.setDescription(e.getClass().getCanonicalName());
        super.setMessage(message(self,e.getMessage()));
        attachInfo(e,debug);
    }


    public ErrorRestResponse(AccessDeniedException ade, boolean debug, String self){
        super.setCode(ACCESS_DENIED_ERROR_CODE);
        super.setDescription("Access denied");
        super.setMessage(message(self,ade.getMessage()));
        attachInfo(ade,debug);
    }


    public ErrorRestResponse(ConstraintViolationException ce, boolean debug, String self){
        super.setCode(VALIDATION_ERROR_CODE);
        Set<ConstraintViolation<?>> cv= ce.getConstraintViolations();
        super.setDescription("Validation Error");
        super.setMessage(self+"Validation failed. " + cv.size() + " error(s)");
        info = new LinkedList<>();
        for(ConstraintViolation<?> v : cv){
            info.add(v.getMessage());
        }
        attachInfo(ce,debug);

    }

    public ErrorRestResponse(MethodArgumentNotValidException me, boolean debug, String self){
        super.setCode(METHOD_ARGUMENT_ERROR_CODE);
        super.setDescription("Validation Error");
        BindingResult br = me.getBindingResult();
        super.setMessage(self+"Validation failed. " + br.getErrorCount() + " error(s)");
        info = new LinkedList<>();
        for (ObjectError objectError : br.getAllErrors()) {
            if(objectError instanceof FieldError) {
                FieldError fError = (FieldError) objectError;
                info.add(fError.getField() + " - " + objectError.getDefaultMessage());
            }
        }
        attachInfo(me,debug);

    }

    @Override
    public List<Object> getInfo() {
        return info;
    }

    public void setInfo(List<Object> info) {
        this.info = info;
    }

    public ErrorRestResponse getCause() {
        return cause;
    }

    public void setCause(ErrorRestResponse cause) {
        this.cause = cause;
    }

    public ApplicationException toApplicationException(){
        ApplicationException applicationException = new ApplicationException(this.getMessage());
        applicationException.setCauseCode(this.getCode());
        applicationException.setCauseDescription(this.getDescription());
        applicationException.setInfo(this.getInfo());
        return applicationException;
    }

    public ServerErrorException toServerErrorException(){
        ServerErrorException serverErrorException = new ServerErrorException(this.getMessage());
        serverErrorException.setCauseCode(this.getCode());
        serverErrorException.setCauseDescription(this.getDescription());
        serverErrorException.setInfo(this.getInfo());
        return serverErrorException;
    }

}
