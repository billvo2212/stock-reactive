package com.example.stockreactive.service;

import com.example.stockreactive.model.Stock;
import com.example.stockreactive.model.StockPrice;
import com.example.stockreactive.repository.StockRepository;
import com.example.stockreactive.repository.StockPriceRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class StockService {

    private final StockRepository stockRepository;
    private final StockPriceRepository stockPriceRepository;
    private final StockApiClient stockApiClient;

    public StockService(StockRepository stockRepository,
                        StockPriceRepository stockPriceRepository,
                        StockApiClient stockApiClient) {
        this.stockRepository = stockRepository;
        this.stockPriceRepository = stockPriceRepository;
        this.stockApiClient = stockApiClient;
    }

    // Fetch stock data from API and store in DB
    public Mono<StockPrice> fetchAndStoreStock(String symbol) {
        return stockApiClient.fetchStockData(symbol);
    }

    // Get all stored stocks
    public Flux<Stock> getAllStocks() {
        return stockRepository.findAll();
    }

    // Get all stored stock prices
    public Flux<StockPrice> getAllStockPrices() {
        return stockPriceRepository.findAll();
    }
}
