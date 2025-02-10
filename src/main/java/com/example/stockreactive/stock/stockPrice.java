package com.example.stockreactive.stock;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="stockPrice")
public class stockPrice {

    @Id
    private String symbol;

    private float price;

    private float dividendsPerShare;
}
