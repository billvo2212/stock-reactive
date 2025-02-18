package com.example.stockreactive.controller;

import java.util.*;
import com.example.stockreactive.model.Stock;
import com.example.stockreactive.model.StockPrice;
import com.example.stockreactive.service.StockService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/stocks")
public class StockController {

    private final StockService stockService;



    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping("/hello")
    public Mono<String> hello() {
        return Mono.just("Hello World AGAIN");
    }

    // Fetch and store stock data
    @GetMapping("/fetch/{symbol}")
    public Mono<StockPrice> fetchAndStoreStock(@PathVariable String symbol) {
        return stockService.fetchAndStoreStock(symbol);
    }

    // Get all stored stocks
    @GetMapping
    public Flux<Stock> getAllStocks() {
        return stockService.getAllStocks();
    }

    // Get all stored stock prices
    @GetMapping("/prices")
    public Flux<StockPrice> getAllStockPrices() {
        return stockService.getAllStockPrices();
    }
}
