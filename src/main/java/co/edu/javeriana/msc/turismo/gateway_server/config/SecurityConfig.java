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
                .configurationSource(request -> {
                    CorsConfiguration corsCfg = new CorsConfiguration();

                    // Permitir cualquier origen (puedes especificar los necesarios)
                    corsCfg.addAllowedOriginPattern("*");

                    // Permitir todos los métodos HTTP (o especificar los necesarios)
                    corsCfg.addAllowedMethod(CorsConfiguration.ALL);

                    // Permitir encabezados necesarios para CORS (puedes ajustar según tus necesidades)
                    corsCfg.addAllowedHeader("*");

                    // Permitir credenciales si es necesario (e.g., cookies o headers de autenticación)
                    corsCfg.setAllowCredentials(true);

                    return corsCfg;
                }))
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
                        .pathMatchers(HttpMethod.GET, "/service-publication-microservice/services/**")
                        .hasRole("SUPPLIER")
                        // Order management microservice
                        .pathMatchers(HttpMethod.GET, "/order-management-microservice/cart/**")
                        .hasAnyRole("CUSTOMER")
                        .pathMatchers(HttpMethod.POST, "/order-management-microservice/cart/**")
                        .hasAnyRole("CUSTOMER")
                        .pathMatchers(HttpMethod.PUT, "/order-management-microservice/cart/**")
                        .hasAnyRole("CUSTOMER")
                        .pathMatchers(HttpMethod.DELETE, "/order-management-microservice/cart/**")
                        .hasAnyRole("CUSTOMER")

                        .pathMatchers(HttpMethod.GET, "/order-management-microservice/order/**")
                        .hasAnyRole("CUSTOMER")
                        .pathMatchers(HttpMethod.POST, "/order-management-microservice/order/**")
                        .hasAnyRole("CUSTOMER")
                        .pathMatchers(HttpMethod.PUT, "/order-management-microservice/order/**")
                        .hasAnyRole("CUSTOMER")
                        .pathMatchers(HttpMethod.DELETE, "/order-management-microservice/order/**")
                        .hasAnyRole("CUSTOMER")

                        //Payment microservice
                        .pathMatchers(HttpMethod.POST, "/payment-microservice/payments")
                        .hasAnyRole("CUSTOMER")

                        //Service rating microservice
                        .pathMatchers(HttpMethod.GET, "/service-rating-microservice/questions/")
                        .hasAnyRole("CUSTOMER", "SUPPLIER")
                        .pathMatchers(HttpMethod.POST, "/service-rating-microservice/questions/")
                        .hasAnyRole("CUSTOMER", "SUPPLIER")
                        .pathMatchers(HttpMethod.PUT, "/service-rating-microservice/questions/")
                        .hasAnyRole("CUSTOMER", "SUPPLIER")
                        .pathMatchers(HttpMethod.DELETE, "/service-rating-microservice/questions/")
                        .hasAnyRole("CUSTOMER", "SUPPLIER")
                        // For any other request, the user must be authenticated
                        .anyExchange().authenticated())
                // Configures JWT to properly process Keycloak tokens
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(
                                new KeycloakJwtAuthenticationConverter())))
                .build();
    }

}