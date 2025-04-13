-- Create a new schema
CREATE SCHEMA IF NOT EXISTS store;

-- Create the product table in the new schema
CREATE TABLE store.product (
                               id BIGINT PRIMARY KEY,
                               name VARCHAR(255),
                               category VARCHAR(255),
                               price DOUBLE PRECISION,
                               isOfferApplied BOOLEAN,
                               discountPercentage DOUBLE PRECISION,
                               priceAfterDiscount DOUBLE PRECISION
);
