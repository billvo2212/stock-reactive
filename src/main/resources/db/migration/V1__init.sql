CREATE TABLE stocks(
    symbol VARCHAR(50) NOT NULL PRIMARY KEY,
    longName VARCHAR(250),
    currency VARCHAR(10),
    quoteSourceName VARCHAR(250),
    marketCap INTEGER
);

DROP TABLE IF EXISTS stockPrice;

CREATE TABLE stockPrice (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY, -- ✅ New primary key
    symbol VARCHAR(50) NOT NULL, -- ✅ Foreign key referencing `stocks(symbol)`
    price DECIMAL(10, 2),
    dividendsPerShare DECIMAL(10, 2),
    FOREIGN KEY (symbol) REFERENCES stocks(symbol) ON DELETE CASCADE
);
