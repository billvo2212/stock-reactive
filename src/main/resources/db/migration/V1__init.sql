CREATE TABLE stocks(
    symbol VARCHAR(50) NOT NULL PRIMARY KEY,
    longName VARCHAR(250),
    currency VARCHAR(10),
    quoteSourceName VARCHAR(250),
    marketCap INTEGER
);

CREATE TABLE stockPrice (
    symbol VARCHAR(50) PRIMARY KEY,
    price DECIMAL(10, 2),
    dividendsPerShare DECIMAL(10, 2),
    FOREIGN KEY (symbol) REFERENCES stocks(symbol)
);