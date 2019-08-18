package com.holkiew.yomenik.battlesim.configuration.cloud;

import com.holkiew.yomenik.battlesim.configuration.cloud.model.Principal;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.BindingContext;
import org.springframework.web.reactive.result.method.HandlerMethodArgumentResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Component
public class UserObjectFromHeaderResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(Principal.class);
    }

    @Override
    public Mono<Object> resolveArgument(MethodParameter parameter, BindingContext bindingContext, ServerWebExchange exchange) {
        HttpHeaders headers = exchange.getRequest().getHeaders();
        try {
            var id = Optional.ofNullable(headers.getFirst("PRINCIPAL-ID")).orElseThrow(Exception::new);
            var username = Optional.ofNullable(headers.getFirst("PRINCIPAL-NAME")).orElseThrow(Exception::new);
            return Mono.just(new Principal(id, username));
        } catch (Exception e) {
            throw new UserHeaderResolverException();
        }
    }


    class UserHeaderResolverException extends RuntimeException {
        UserHeaderResolverException() {
            super("Can't resolve user header username and/or id");
        }
    }
}
