package com.example.apigatewayservice.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class CustomFilter extends AbstractGatewayFilterFactory<CustomFilter.Config> {

    public CustomFilter(){
        super(Config.class);

    }
    public static class Config{
        /// configuration properties 넣기

    }
    @Override
    public GatewayFilter apply(CustomFilter.Config config) {
        // Pre Filter 커스텀 하기
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();
            log.info("Custom PRE filter: request id -> {}", request.getId());

            // Custom Post Filter
//            Mono는 0-1개의 결과만을 처리하기 위한 Reactor 객체
//            Flux는 0-N개의 결과물을 처리하기 위한 Reactor 객체

            return chain.filter(exchange).then(Mono.fromRunnable(()->{
                log.info("Custom POST filter: response code -> {}", response.getStatusCode());
            }));
        };
    }
}
