package com.holkiew.yomenik.battlesim.configuration.webflux;

import com.holkiew.yomenik.battlesim.configuration.webflux.model.Principal;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.BindingContext;
import org.springframework.web.reactive.result.method.HandlerMethodArgumentResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Component
public class UserObjectFromHeaderResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(Principal.class);
    }

    @Override
    public Mono<Object> resolveArgument(MethodParameter parameter, BindingContext bindingContext, ServerWebExchange exchange) {
        return Mono.just(exchange.getRequest().getHeaders())
                .map(httpHeaders -> {
                    var id = httpHeaders.getFirst("PRINCIPAL-ID");
                    var username = httpHeaders.getFirst("PRINCIPAL-NAME");
                    if (Objects.isNull(id) || Objects.isNull(username)) {
                        return Mono.error(new UserHeaderResolverException());
                    }
                    return new Principal(id, username);
                });
    }


    class UserHeaderResolverException extends RuntimeException {
        UserHeaderResolverException() {
            super("Can't resolve user header username and/or id");
        }
    }
}
