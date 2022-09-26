package com.example.apigatewayservice.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.impl.JWTParser;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {
Environment env;

    public AuthorizationHeaderFilter(Environment env) {
        super(Config.class);
        this.env = env;
    }

    // login -> token -> users (with token) -> header(include token)
    public static class Config{


    }
    @Override
    public GatewayFilter apply(AuthorizationHeaderFilter.Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
          if(!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)){
              return onError(exchange, "No authorization header", HttpStatus.UNAUTHORIZED);
          }
          String authorizationHeader = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
          String jwt = authorizationHeader.replace("Bearer ", "");
          if(!isJwtValid(jwt)){
              return onError(exchange, "JWT token is not valid", HttpStatus.UNAUTHORIZED);
          }
        return chain.filter(exchange);
        };
    }

    private boolean isJwtValid(String jwt) {
        boolean returnValue = true;

        String subject = null;
        try{

            DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(env.getProperty("token.secret"))).build().verify(jwt);

            subject = decodedJWT.getSubject();
            decodedJWT.getExpiresAt();

        }catch (Exception ex){
            returnValue = false;
        }
        if(subject == null || subject.isEmpty()){
            returnValue = false;
        }
        return returnValue;
    }

    // Mono, Flux -> Spring WebFlux -> reactive 한 것을 위해 새로 생김
    // servlet 안 씀 -> serverhttpResponse 로 대체
    private Mono<Void> onError(ServerWebExchange exchange, String no_authorization_header, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);

        log.error(no_authorization_header);
        return response.setComplete();
    }
}
