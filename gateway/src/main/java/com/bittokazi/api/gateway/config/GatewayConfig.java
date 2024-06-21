package com.bittokazi.api.gateway.config;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * @author Bitto Kazi
 */
@Component
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r.path("/app/**")
                        .filters(f ->{
                            return f.preserveHostHeader();
                        })
                        .uri(System.getenv().getOrDefault("GATEWAY_FRONTEND_SERVICE", "http://127.0.0.1:4200"))
                )
                .route(r -> r.path("/**")
                        .filters(f ->{
                            return f.preserveHostHeader();
                        })
                        .uri(System.getenv().getOrDefault("GATEWAY_BACKEND_SERVICE", "http://127.0.0.1:5010"))
                ).build();
    }
}
