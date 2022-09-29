package com.nanum.apigatewayservice.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

@Component
@Slf4j
public class CustomFilter extends AbstractGatewayFilterFactory<CustomFilter.Config> {

    public CustomFilter(){
        super(Config.class);
    }
    public static class Config{
        /// configuration properties 넣기

    }

    //토큰을 검증
    @Override
    public GatewayFilter apply(CustomFilter.Config config) {
        // Pre Filter 커스텀 하기
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest(); //Pre Filter
            log.info(String.valueOf(request.getHeaders().get("Authorization")));
            if (!request.getHeaders().containsKey("Authorization")) {
                return handleUnAuthorized(exchange);
            }

            //token에서 문자열 받아오기
            List<String> token = request.getHeaders().get("Authorization");
            String tokenString = Objects.requireNonNull(token).get(0);
            log.info(tokenString);

            // 토큰 검증
            if(!tokenString.equals("A.B.C")) {
                return handleUnAuthorized(exchange); // 토큰이 일치하지 않을 때
            }
            return chain.filter(exchange);
            // Custom Post Filter
//            Mono는 0-1개의 결과만을 처리하기 위한 Reactor 객체
//            Flux는 0-N개의 결과물을 처리하기 위한 Reactor 객체
        };
    }

    private Mono<Void> handleUnAuthorized(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();

        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response.setComplete();
    }

}
