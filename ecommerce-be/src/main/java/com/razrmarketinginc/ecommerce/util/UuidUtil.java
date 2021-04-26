package com.razrmarketinginc.ecommerce.util;

import com.google.common.io.BaseEncoding;
import org.apache.commons.lang3.StringUtils;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.UUID;

public class UuidUtil {

    static BaseEncoding base32 = BaseEncoding.base32().omitPadding();
    static BaseEncoding base64url = BaseEncoding.base64Url().omitPadding();

    public static BigInteger getBigIntegerFromUuid(UUID randomUUID) {
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(randomUUID.getMostSignificantBits());
        bb.putLong(randomUUID.getLeastSignificantBits());
        return new BigInteger(1, bb.array());
    }

    public static String toBase32(UUID uuid) {
    	return base32.encode(uuidToBytes(uuid));
    }

    public static String randomBase32(){
        return toBase32(UUID.randomUUID());
    }

    public static String randomBase64AlphaNumeric(){
        return toBase64AlphaNumeric(UUID.randomUUID());
    }

    public static String toBase64(UUID uuid){

        return base64url.encode(uuidToBytes(uuid));
    }

    public static String toBase64AlphaNumeric(UUID uuid){
        String base64 = UuidUtil.toBase64(uuid);
        return StringUtils.replaceChars(base64,
                "-_", StringUtils.replaceChars(base64,"-_",""));
    }

    public static byte[] uuidToBytes(UUID uuid) {
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        return bb.array();
    }


}
