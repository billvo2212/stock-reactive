package com.example.stockreactive.repository;

import com.example.stockreactive.model.StockPrice;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface StockPriceRepository extends ReactiveCrudRepository<StockPrice, String> {
    Mono<StockPrice> findBySymbol(String symbol);
    Flux<StockPrice> findAll();
}
