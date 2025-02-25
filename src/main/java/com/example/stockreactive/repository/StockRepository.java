package com.example.stockreactive.repository;

import com.example.stockreactive.model.Stock;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface StockRepository extends ReactiveCrudRepository<Stock, String> {
    Mono<Stock> findBySymbol(String symbol);
}
