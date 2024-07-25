package com.ecommerce.gateway.filters;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;

import com.ecommerce.gateway.dto.TokenResource;
import com.ecommerce.gateway.utils.Constants;

@Component
public class TokenValidatorFilter extends AbstractGatewayFilterFactory<TokenValidatorFilter.Config> {

    private WebClient.Builder webClientBuilder;

    public TokenValidatorFilter(WebClient.Builder webClientBuilder) {
        super(Config.class);
        this.webClientBuilder = webClientBuilder;
    }

    public static class Config {
        // Configuration properties can be added here if needed
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String token = request.getHeaders().getFirst(Constants.AUTHORIZATION_HEADER);
            if (token == null || token.isEmpty()) {
                return null;
                // throw new UnAuthorizedError("Authentication failure");
            }
            return webClientBuilder.build()
                    .post()
                    .uri(Constants.AUTH_SERVICE_TOKEN_VERIFICATION_ENDPOINT)
                    .header(Constants.AUTHORIZATION_HEADER, token)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                    .retrieve()
                    .onStatus(HttpStatusCode::is5xxServerError,response ->{
                        System.out.println(response);
                        return null;
                        // throw new InternalServerError("Server error", response);
                    })
                    .onStatus(HttpStatusCode::is4xxClientError,  response ->{
                        System.out.println(response);
                        return null;
                        // throw new ClientError("Client Error", response);
                    })
                    .bodyToMono(TokenResource.class)
                    .flatMap(data -> {
                        ServerWebExchange modifiedExchange = exchange.mutate()
                        .request(r -> r.headers(headers -> {
                            headers.add("X-userId", data.getId());
                        }))
                        .build();
                    return chain.filter(modifiedExchange);
                    });
        };
    }
    
}