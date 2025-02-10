package com.example.stockreactive.stock;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="stocks")
public class stocks {

    @Id
    private String symbol;

    private String longName;

    private String currency;

    private String quoteSourceName;

    private int marketCap;
}