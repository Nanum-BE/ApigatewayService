package com.nanum.apigatewayservice.filter;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class GlobalFilter extends AbstractGatewayFilterFactory<GlobalFilter.Config> {

    public GlobalFilter(){
        super(Config.class);

    }

    @Override
    public GatewayFilter apply(GlobalFilter.Config config) {
        // Pre Filter 커스텀 하기
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();
            log.info("Global Filter baseMessage:/·// {}", config.getBaseMessage());

            if(config.isPreLogger()){
                log.info("Global Filter Start: request id -> {}",request.getId());
            }
            // Custom Post Filter
//            Mono는 0-1개의 결과만을 처리하기 위한 Reactor 객체
//            Flux는 0-N개의 결과물을 처리하기 위한 Reactor 객체

            return chain.filter(exchange).then(Mono.fromRunnable(()->{
                if(config.isPostLogger()) {
                    log.info("Global End filter: response code -> {}", response.getStatusCode());
                }
            }));
        };
    }
    @Data
    public static class Config{
        /// configuration properties 넣기
        private String baseMessage;
        private boolean preLogger;
        private boolean postLogger;
    }
}
