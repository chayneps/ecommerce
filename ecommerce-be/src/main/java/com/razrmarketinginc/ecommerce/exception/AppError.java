package com.razrmarketinginc.ecommerce.exception;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum AppError implements Error {

    ACCOUNT_NOT_FOUND(100000,"Account not found"),
    ACCOUNT_NUMBER_DUPLICATED(100001,"AccountNumber duplicated"),
    POINT_NOT_ENOUGH(100002, "Not enough point to redeem"),
    TRAN_NOT_FOUND(100003,"Transaction not found"),
    ONLY_CAN_RETURN_REDEMPTION(100004,"Can only return redemption"),
    ORG_TRAN_HAS_BEEN_RETURN(100005,"The original redemption transaction has been returned"),
    TRANSACTION_FAIL(100006,"Fail transaction"),


    UNKNOWN_ERROR(100999,"Unknown Error")
    ;

    private final Integer errorCode;
    private final String errorDescription;

    private static Map<Integer, AppError> mMap;

    AppError(Integer errorCode, String errorDescription) {
        this.errorCode = errorCode;
        this.errorDescription = errorDescription;
    }

    @Override
    public Integer getErrorCode() {
        return errorCode;
    }

    @Override
    public String getErrorDescription(){
        return errorDescription;
    }

    public static AppError get(Integer code) {
        if(mMap==null){
            synchronized (AppError.class) {
                if (mMap == null)
                    mMap = Collections.unmodifiableMap(initializeMapping());
            }
        }

        if (mMap.containsKey(code)) {
            return mMap.get(code);
        }
        return null;

    }

    private static Map<Integer, AppError> initializeMapping() {

        Map<Integer, AppError> mMap = new HashMap<>();
        for (AppError e : AppError.values()) {
            mMap.put(e.errorCode, e);
        }
        return mMap;
    }
}
