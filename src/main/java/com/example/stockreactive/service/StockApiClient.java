//package com.example.stockreactive.service;
//
//import com.example.stockreactive.model.Stock;
//import com.example.stockreactive.model.StockPrice;
//import com.example.stockreactive.repository.StockRepository;
//import com.example.stockreactive.repository.StockPriceRepository;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.asynchttpclient.AsyncHttpClient;
//import org.asynchttpclient.DefaultAsyncHttpClient;
//import org.asynchttpclient.Response;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import reactor.core.publisher.Mono;
//
//import java.util.concurrent.CompletableFuture;
//
//@Service
//public class StockApiClient {
//
//    private final AsyncHttpClient httpClient = new DefaultAsyncHttpClient();
//    private final ObjectMapper objectMapper = new ObjectMapper();
//    private final StockRepository stockRepository;
//    private final StockPriceRepository stockPriceRepository;
//
//    @Value("${rapidapi.key}")
//    private String rapidApiKey;
//
//    @Value("${rapidapi.host}")
//    private String rapidApiHost;
//
//    public StockApiClient(StockRepository stockRepository, StockPriceRepository stockPriceRepository) {
//        this.stockRepository = stockRepository;
//        this.stockPriceRepository = stockPriceRepository;
//    }
//
//    public Mono<StockPrice> fetchStockData(String symbol) {
//        String requestUrl = "https://rapidapi.com/api/v1/markets/stock/quotes?ticker=" + symbol;
//
//        // Asynchronous API call
//        CompletableFuture<Response> futureResponse = httpClient.prepareGet(requestUrl)
//                .setHeader("x-rapidapi-key", rapidApiKey)
//                .setHeader("x-rapidapi-host", rapidApiHost)
//                .execute()
//                .toCompletableFuture();
//
//        return Mono.fromFuture(futureResponse)
//                .flatMap(response -> {
//                    try {
//                        if (response.getStatusCode() != 200) {
//                            return Mono.error(new RuntimeException("Error fetching stock data: " + response.getStatusText()));
//                        }
//
//                        JsonNode jsonResponse = objectMapper.readTree(response.getResponseBody());
//
//                        System.out.println("THIS IS JSON RESPONSE: " + jsonResponse);
//
//                        // Extract stock details
//                        Stock stock = new Stock(
//                                jsonResponse.get("quotes").get(0).get("symbol").asText(),
//                                jsonResponse.get("quotes").get(0).get("longName").asText(),
//                                jsonResponse.get("quotes").get(0).get("currency").asText(),
//                                jsonResponse.get("quotes").get(0).get("quoteSourceName").asText(),
//                                jsonResponse.get("quotes").get(0).get("marketCap").asInt()
//                        );
//
//                        // Extract stock price
//                        StockPrice stockPrice = new StockPrice(
//                                jsonResponse.get("quotes").get(0).get("symbol").asText(),
//                                (float) jsonResponse.get("quotes").get(0).get("regularMarketPrice").asDouble(),
//                                (float) jsonResponse.get("quotes").get(0).get("dividendsPerShare").asDouble()
//                        );
//
//                        // Save to database
//                        return stockRepository.save(stock)
//                                .then(stockPriceRepository.save(stockPrice));
//                    } catch (Exception e) {
//                        return Mono.error(new RuntimeException("Error parsing JSON response", e));
//                    }
//                });
//    }
//}

package com.example.stockreactive.service;

import com.example.stockreactive.model.Stock;
import com.example.stockreactive.model.StockPrice;
import com.example.stockreactive.repository.StockRepository;
import com.example.stockreactive.repository.StockPriceRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.Response;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class StockApiClient {
//    @Autowired
//    private final R2dbcEntityTemplate r2dbcEntityTemplate;

    private final AsyncHttpClient httpClient = new DefaultAsyncHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final StockRepository stockRepository;
    private final StockPriceRepository stockPriceRepository;

    @Value("${rapidapi.key}")
    private String rapidApiKey;

    @Value("${rapidapi.host}")
    private String rapidApiHost;

    public StockApiClient(R2dbcEntityTemplate r2dbcEntityTemplate, StockRepository stockRepository, StockPriceRepository stockPriceRepository) {
//        this.r2dbcEntityTemplate = r2dbcEntityTemplate;
        this.stockRepository = stockRepository;
        this.stockPriceRepository = stockPriceRepository;
    }

    @Transactional
    public Mono<StockPrice> fetchStockData(String symbol) {
        // ✅ FIXED: Correct API URL
        String requestUrl = "https://yahoo-finance15.p.rapidapi.com/api/v1/markets/stock/quotes?ticker=" + symbol;

        CompletableFuture<Response> futureResponse = httpClient.prepareGet(requestUrl)
                .setHeader("x-rapidapi-key", rapidApiKey)
                .setHeader("x-rapidapi-host", rapidApiHost)
                .execute()
                .toCompletableFuture();

        return Mono.fromFuture(futureResponse)
                .flatMap(response -> {
                    try {
                        int statusCode = response.getStatusCode();

                        // ✅ Handle HTTP errors gracefully
                        if (statusCode == 403) {
                            return Mono.error(new RuntimeException("ERROR: 403 Forbidden - Invalid API key."));
                        }
                        if (statusCode != 200) {
                            return Mono.error(new RuntimeException("ERROR: API request failed with status " + statusCode));
                        }

                        JsonNode jsonResponse = objectMapper.readTree(response.getResponseBody());



                        // ✅ Ensure "quotes" exists and is not empty
                        if(jsonResponse.get("body").isEmpty()){
                            return Mono.error(new RuntimeException("ERROR: Missing 'quotes' data in API response."));
                        }
                        JsonNode stockNode = jsonResponse.get("body").get(0);

                        // ✅ Log the full API response
                        System.out.println("**********THIS IS JSON RESPONSE: " + stockNode.toString());

                        Stock stock = new Stock(
                                stockNode.get("symbol").asText(),
                                stockNode.has("longName") ? stockNode.get("longName").asText() : "N/A",
                                stockNode.has("currency") ? stockNode.get("currency").asText() : "N/A",
                                stockNode.has("quoteSourceName") ? stockNode.get("quoteSourceName").asText() : "Unknown",
                                stockNode.has("marketCap") ? stockNode.get("marketCap").asInt() : 0
                        );
                        // ✅ Extract and clean the price (removing "$" if present)
                        float cleanedPrice = parsePrice(stockNode.has("regularMarketPrice") ? stockNode.get("regularMarketPrice").asText() : "0.0");
                        float cleanedDividends = parsePrice(stockNode.has("dividendsPerShare") ? stockNode.get("dividendsPerShare").asText() : "0.0");

                        StockPrice stockPrice = new StockPrice(
                                UUID.randomUUID(), // ✅ Generate new UUID
                                stock.getSymbol(),
                                cleanedPrice,
                                cleanedDividends
                        );


                        return stockRepository.save(stock)
                                .then(stockPriceRepository.save(stockPrice));




                    } catch (Exception e) {
                        return Mono.error(new RuntimeException("ERROR: Failed to parse API response", e));
                    }
                });

    }
    private float parsePrice(String priceString) {
        Pattern pattern = Pattern.compile("[0-9]+(\\.[0-9]+)?");  // Matches numbers like "12.34" or "100"
        Matcher matcher = pattern.matcher(priceString);

        if (matcher.find()) {
            return Float.parseFloat(matcher.group());
        }
        return 0.0f;
    }
}
