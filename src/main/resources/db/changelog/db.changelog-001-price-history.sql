-- db.changelog-001-price-history.sql
-- This table tracks the hourly price of Bitcoin in USD.
-- Columns: id (auto increment), timestamp (datetime), price_usd (decimal)
CREATE TABLE price_history
(
    id        SERIAL PRIMARY KEY,
    timestamp TIMESTAMP NOT NULL,
    price_usd DECIMAL(20, 8) NOT NULL
);
