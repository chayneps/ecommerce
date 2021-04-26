package com.razrmarketinginc.ecommerce.exception;

import java.util.List;

public class ServerErrorException extends ApplicationException{
    public ServerErrorException(String message) {
        super(message);
    }

    public ServerErrorException(Error error) {
        super(error);
    }

    public ServerErrorException(String message, Error error) {
        super(message, error);
    }

    public ServerErrorException(Throwable t, Error error) {
        super(t, error);
    }

    public ServerErrorException(String message, Throwable t, Error error) {
        super(message, t, error);
    }

    public ServerErrorException(String message, Throwable t, Error error, List<Object> info) {
        super(message,t,error,info);
    }
}
