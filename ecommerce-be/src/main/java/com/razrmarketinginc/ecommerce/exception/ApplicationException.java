package com.razrmarketinginc.ecommerce.exception;

import java.util.List;

public class ApplicationException extends RuntimeException {

    private Error error;
    private String causeCode;
    private String causeDescription;
    private String causeMessage;
    private List<Object> info;

    public ApplicationException(String message) {
        super(message);
    }

    public ApplicationException(Error error) {
        this.error = error;
    }

    public ApplicationException(String message, Error error) {
        super(message);

        this.error = error;
    }

    public ApplicationException(Throwable t, Error error) {
        super(t);

        this.error = error;
    }

    public ApplicationException(String message, Throwable t, Error error, List<Object> info) {
        this(message, t,error);

        this.info = info;
    }

    public ApplicationException(String message, Throwable t, Error error) {
        super(message, t);

        this.error = error;
    }



    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }


    public String getCauseCode() {
        return causeCode;
    }

    public void setCauseCode(String causeCode) {
        this.causeCode = causeCode;
    }

    public String getCauseDescription() {
        return causeDescription;
    }

    public void setCauseDescription(String causeDescription) {
        this.causeDescription = causeDescription;
    }

    public String getCauseMessage() {
        return causeMessage;
    }

    public void setCauseMessage(String causeMessage) {
        this.causeMessage = causeMessage;
    }

    public List<Object> getInfo() {
        return info;
    }

    public void setInfo(List<Object> info) {
        this.info = info;
    }

    @Override
    public String toString() {
        if(error!=null)
            if (error.getErrorCode() == null) {
                return this.error.getErrorDescription()
                        + ((this.getMessage() == null) ? "" : "(" + this.getMessage() + ")");
            } else {
                return this.error.getErrorCode().toString() + " - " + this.error.getErrorDescription()
                        + ((this.getMessage() == null) ? "" : "(" + this.getMessage() + ")");
            }
        else
            return this.getCauseCode() + " - " + this.getCauseDescription()
                    + ((this.getCauseMessage() == null) ? "" : "(" + this.getCauseMessage() + ")");
    }
}
