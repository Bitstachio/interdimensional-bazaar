-- V4: Add product_name and product_slug to order_items to match JPA Entity
-- Run this to fix the "missing column [product_name]" and "missing column [product_slug]" errors

USE bazaar_db;

ALTER TABLE order_items
    ADD COLUMN product_name VARCHAR(255) NOT NULL AFTER product_id,
    ADD COLUMN product_slug VARCHAR(255) NOT NULL AFTER product_name;