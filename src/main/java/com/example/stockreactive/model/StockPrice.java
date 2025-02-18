package com.example.stockreactive.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "stockPrice") // Ensure the table name matches your schema
public class StockPrice {

    @Id
    private UUID id; // New primary key (UUID)

    @Column("symbol") // Foreign key referencing `stocks(symbol)`
    private String symbol;

    private float price;

    private float dividendsPerShare;
}
