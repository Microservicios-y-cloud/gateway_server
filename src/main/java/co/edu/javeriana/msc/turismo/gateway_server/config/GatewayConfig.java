package co.edu.javeriana.msc.turismo.gateway_server.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {
    @Bean
    RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("keycloak_route", r -> r
                        // Paths to rewrite
                        .path("/keycloak-server/**") 
                        // Rewrite the path to remove /keycloak-server
                        .filters(f -> f.rewritePath("/keycloak-server/(?<segment>.*)", "/${segment}"))
                        // Forward to Keycloak server running on localhost:9000
                        // TODO Obtain this data from application.yml
                        .uri("http://localhost:9000")) 
                .build();
    }
}
