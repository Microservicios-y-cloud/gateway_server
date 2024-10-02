package co.edu.javeriana.msc.turismo.gateway_server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .cors(cors -> cors
                        .configurationSource(request -> new CorsConfiguration()
                                .applyPermitDefaultValues()))
                .csrf(csrf -> csrf
                        .disable())
                .authorizeExchange(exchange -> exchange
                        // Allow authentication endpoint
                        .pathMatchers(HttpMethod.POST, "/keycloak-server/realms/ecommerce/protocol/openid-connect/token")
                        .permitAll()

                        // Allow access to simple microservice to certain roles
                        //TODO check all routes
                        .pathMatchers(HttpMethod.GET, "/service-query-microservice/services/**")
                        .hasAnyRole("CUSTOMER", "SUPPLIER")
                        .pathMatchers(HttpMethod.GET, "/service-query-microservice/graphql/**")
                        .hasRole("CUSTOMER")


                        // For any other request, the user must be authenticated
                        .anyExchange().authenticated())
                // Configures JWT to properly process Keycloak tokens
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(
                                new KeycloakJwtAuthenticationConverter())))
                .build();
    }

}