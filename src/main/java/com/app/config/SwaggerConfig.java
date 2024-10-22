package com.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfig {

	// Swagger UI 화면 표시 설정
    private Info apiInfo() {
        return new Info()
                .title("FreeMeal URL 테스트")
                .description("프론트에서 유저 및 인증 등 요청에 관한 REST API")
                .version("1.0.0");
    }
	
    // SecuritySecheme 설정
    private String jwtSchemeName = "jwtAuth";

    // API 요청헤더에 인증정보 포함
    private SecurityRequirement securityRequirement() {
        return new SecurityRequirement().addList(jwtSchemeName);
    }

    // SecuritySechemes 등록
    private Components jwtComponent() {
        return new Components()
            .addSecuritySchemes(jwtSchemeName, 
                new SecurityScheme()
                    .name(jwtSchemeName)
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer").bearerFormat("JWT")
            );
    }

	@Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(apiInfo())
                .addSecurityItem(securityRequirement())
                .components(jwtComponent());
    }
	
}
