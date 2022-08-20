package com.github.andrielson.spring.webflux.order.client;

import com.github.andrielson.spring.webflux.order.dto.ProductDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

@Service
public class ProductClient {
    private final WebClient webClient;

    public ProductClient(@Value("${product.service.url}") String url) {
        this.webClient = WebClient.builder().baseUrl(url).build();
    }

    public Mono<ProductDto> getProductById(final String productId) {
        return this.webClient.get()
                .uri("{id}", productId)
                .retrieve()
                .bodyToMono(ProductDto.class)
                .retryWhen(Retry.fixedDelay(5, Duration.ofSeconds(1)));
    }
}
