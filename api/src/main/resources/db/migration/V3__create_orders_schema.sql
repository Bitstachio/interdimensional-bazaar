-- V3: Create orders and order_items tables to satisfy JPA validation
-- Run after V2__add_missing_columns.sql

USE bazaar_db;

-- Orders Table
-- Tracks the overall transaction, status, and link to the user/address
CREATE TABLE orders
(
    id                  VARCHAR(36)    NOT NULL PRIMARY KEY,
    user_id             VARCHAR(36)    NOT NULL,
    shipping_address_id VARCHAR(36)    NOT NULL,
    total_amount        DECIMAL(10, 2) NOT NULL,
    status              ENUM ('pending', 'processing', 'shipped', 'delivered', 'cancelled') DEFAULT 'pending',
    created_at          TIMESTAMP      DEFAULT CURRENT_TIMESTAMP NULL,
    updated_at          TIMESTAMP      DEFAULT CURRENT_TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_order_user FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_order_address FOREIGN KEY (shipping_address_id) REFERENCES addresses (id)
);

-- Order Items Table
-- Snapshots the price at the time of purchase (important as product prices change)
CREATE TABLE order_items
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id   VARCHAR(36)    NOT NULL,
    product_id VARCHAR(36)    NOT NULL,
    quantity   INT            NOT NULL,
    unit_price DECIMAL(10, 2) NOT NULL,
    CONSTRAINT fk_items_order FOREIGN KEY (order_id) REFERENCES orders (id) ON DELETE CASCADE,
    CONSTRAINT fk_items_product_order FOREIGN KEY (product_id) REFERENCES products (id)
);