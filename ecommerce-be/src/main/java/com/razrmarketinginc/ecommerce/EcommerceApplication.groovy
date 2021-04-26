package com.razrmarketinginc.ecommerce

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springdoc.core.SpringDocUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class EcommerceApplication {

    static void main(String[] args) {
        SpringApplication.run(EcommerceApplication, args)
    }

    @Bean
    public OpenAPI customOpenAPI(@Value('${ecommerce.api-version}') String apiVersion) {

        SpringDocUtils.getConfig().removeRequestWrapperToIgnore(Map)

        return new OpenAPI()
                .info(new Info()
                        .title("Ecommerce API")
                        .version(apiVersion))

    }

}
