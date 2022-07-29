package com.ntv.ntvcons_backend.configs;

//import io.swagger.v3.oas.annotations.OpenAPIDefinition;
//import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
//import io.swagger.v3.oas.annotations.info.Contact;
//import io.swagger.v3.oas.annotations.info.Info;
//import io.swagger.v3.oas.annotations.info.License;
//import io.swagger.v3.oas.annotations.security.SecurityRequirement;
//import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
//@OpenAPIDefinition(
//    info = @Info(
//            title = "NiemTinVang Swagger Web",
//            description = "NiemTinVang APIs.",
//            version = "v1.0",
//            /*termsOfService = "",*/
//            contact = @Contact(
//                    name = "Golden Trust",
//                    url = "https://niemtinvang.vn",
//                    email = "goldentrustcons@gmail.com"),
//            license = @License(
//                    name = "Apache 2.0",
//                    url = "https://springdoc.org"))
//)
//@SecurityScheme(
//    name = "Bearer Authentication",
//    type = SecuritySchemeType.HTTP,
//    bearerFormat = "JWT",
//    scheme = "bearer")
/* OpenApi is the standard that Swagger use
 * In short OpenApi = Swagger */
public class OpenApi30Config {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes(
                                "Bearer Authentication",
                                new SecurityScheme()
                                        .name("Authorization")
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .in(SecurityScheme.In.HEADER)))
                .info(new Info()
                        .title("NiemTinVang Swagger Web")
                        .description("NiemTinVang APIs.")
                        .version("v1.0")
                        .contact(new Contact()
                                .name("Golden Trust")
                                .url("https://niemtinvang.vn")
                                .email("goldentrustcons@gmail.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://springdoc.org")))
                .addSecurityItem(new SecurityRequirement()
                        .addList("Bearer Authentication", Arrays.asList("read", "write")));
    }
}
