package com.razrmarketinginc.ecommerce.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.razrmarketinginc.ecommerce.util.JsonUtil;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class DTO {


    public String toPrettyJson() {
        try {
            return JsonUtil.getMapper()
                    .writerWithDefaultPrettyPrinter()
                    .writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    public String toJson() {
        return JsonUtil.marshall(this);
    }

    @Override
    public String toString(){
        return ToStringBuilder.reflectionToString(this);
    }
}
