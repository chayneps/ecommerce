package com.razrmarketinginc.ecommerce

import com.razrmarketinginc.ecommerce.dto.DTO
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties('ecommerce')
class EcommerceProperties extends DTO{
    String apiVersion
    Integer pageSize
}
