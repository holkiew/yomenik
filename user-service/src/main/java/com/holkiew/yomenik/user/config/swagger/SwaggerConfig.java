package com.holkiew.yomenik.user.config.swagger;

import com.google.common.collect.Lists;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.AntPathMatcher;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.AuthorizationScopeBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebFlux;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@Configuration
@EnableSwagger2WebFlux
public class SwaggerConfig {

    private static final String AUTH_POPUP = "Token /auth/signin. Add Bearer before TOKEN";
    private static final String UNSECURED_ENDPOINTS = "/api/auth/**";
    private static final String SWAGGER_ENDPOINTS_SCOPE = "/**";

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
//                .ignoredParameterTypes(LoggedUser.class)
                .securitySchemes(getSecuritySchemes())
                .securityContexts(getSecurityContexts())
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.ant(SWAGGER_ENDPOINTS_SCOPE))
                .build();
    }

    private ArrayList<ApiKey> getSecuritySchemes() {
        return Lists.newArrayList(new ApiKey(AUTH_POPUP, "Authorization", "header"));
    }

    private ArrayList<SecurityContext> getSecurityContexts() {
        return Lists.newArrayList(SecurityContext.builder()
                .securityReferences(Lists.newArrayList(getSecurityReference()))
                .forPaths(getSecuredEndpointsPredicate())
                .build());
    }

    private Predicate<String> getSecuredEndpointsPredicate() {
        return path -> {
            AntPathMatcher matcher = new AntPathMatcher();
            return !matcher.match(UNSECURED_ENDPOINTS, path);
        };
    }

    private SecurityReference getSecurityReference() {
        return SecurityReference.builder()
                .reference(AUTH_POPUP)
                .scopes(getAuthorizationScopes())
                .build();
    }

    private AuthorizationScope[] getAuthorizationScopes() {
        List<AuthorizationScope> authScopeList = new ArrayList<>();
        authScopeList.add(new AuthorizationScopeBuilder().scope("global").description("token required").build());
        return authScopeList.toArray(new AuthorizationScope[0]);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Y O M E N I K")
                .description("APPZ").version("0.1.0").build();
    }
}
