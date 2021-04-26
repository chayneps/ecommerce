package com.razrmarketinginc.ecommerce.util;

import java.sql.Timestamp;
import java.time.Instant;

public class DateUtil {
    public static Timestamp timestampNow(){
        return Timestamp.from(Instant.now());
    }
}
