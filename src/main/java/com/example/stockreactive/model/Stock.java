package com.example.stockreactive.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "stocks")  // ✅ Ensure table name matches
public class Stock {

    @Id
    private String symbol;

    @Column("longName")  // ✅ Explicitly define camelCase column
    private String longName;

    @Column("currency")
    private String currency;

    @Column("quoteSourceName")  // ✅ Explicitly define camelCase column
    private String quoteSourceName;

    @Column("marketCap")  // ✅ Explicitly define camelCase column
    private int marketCap;
}
