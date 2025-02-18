package com.example.stockreactive.repository;

import com.example.stockreactive.model.Stock;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface StockRepository extends ReactiveCrudRepository<Stock, String> {
    Mono<Stock> findBySymbol(String symbol);
}
