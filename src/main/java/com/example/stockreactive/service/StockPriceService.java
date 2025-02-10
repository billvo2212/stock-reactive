package com.example.stockreactive.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class StockPriceService {

    private final WebClient webClient;

    public StockPriceService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://yh-finance.p.rapidapi.com")
                .defaultHeader("x-rapidapi-key", "Sign Up for Key")
                .defaultHeader("x-rapidapi-host", "yh-finance.p.rapidapi.com")
                .build();
    }

    public Mono<String> fetchStockPrices(String symbols) {
        return this.webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/market/v2/get-quotes")
                        .queryParam("region", "US")
                        .queryParam("symbols", symbols)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .onErrorResume(e -> Mono.just("Error fetching stock prices"));
    }
}
