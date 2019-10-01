package com.holkiew.yomenik.battlesim.configuration.webflux;

import org.springframework.core.MethodParameter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.BindingContext;
import org.springframework.web.reactive.result.method.HandlerMethodArgumentResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.Optional;

@Component
public class PageableResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(Pageable.class);
    }

    @Override
    public Mono<Object> resolveArgument(MethodParameter parameter, BindingContext bindingContext, ServerWebExchange exchange) {
        return Mono.just(exchange.getRequest().getQueryParams())
                .map(queryParamsMap -> {
                    int page = Integer.parseInt(Objects.requireNonNull(queryParamsMap.getFirst("page")));
                    int size = Integer.parseInt(Objects.requireNonNull((queryParamsMap.getFirst("size"))));
                    var sort = Optional.ofNullable(queryParamsMap.getFirst("sort"));

                    if (sort.isPresent()) {
                        String[] split = sort.get().split(",");
                        if (split.length == 2) {
                            return PageRequest.of(page, size, new Sort(Sort.Direction.fromString(split[1]), split[0]));
                        }
                    }
                    return PageRequest.of(page, size);
                });
    }
}
