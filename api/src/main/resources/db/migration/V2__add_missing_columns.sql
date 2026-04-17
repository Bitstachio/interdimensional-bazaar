-- V2: Add missing columns to products and users tables, add addresses and reviews tables
-- Run after V1__initial_schema.sql

USE bazaar_db;

-- Products

ALTER TABLE products
    ADD COLUMN sku          VARCHAR(255)                                                     UNIQUE NULL,
    ADD COLUMN rating       DECIMAL(2, 1)                                                   DEFAULT 0 NULL,
    ADD COLUMN danger_level VARCHAR(255)                                                    NULL,
    ADD COLUMN sizes        JSON                                                             NULL;

-- Users

ALTER TABLE users
    ADD COLUMN role  ENUM ('customer', 'admin') DEFAULT 'customer'                          NOT NULL,
    ADD COLUMN phone VARCHAR(20)                                                             NULL;

-- Addresses
-- One row per unique address per user.
-- is_default_delivery and is_default_billing are enforced at application
-- level — when setting a new default, the previous default is unset first.
-- TODO: APPLICATION LEVEL — enforce single default per type in useCheckout.ts
-- and account address management hooks.

CREATE TABLE addresses
(
    id                   VARCHAR(36)  NOT NULL PRIMARY KEY,
    user_id              VARCHAR(36)  NOT NULL,
    full_name            VARCHAR(100) NOT NULL,
    street               VARCHAR(255) NOT NULL,
    city                 VARCHAR(100) NOT NULL,
    province_state       VARCHAR(100) NOT NULL,
    country              CHAR(2)      NOT NULL,
    postal_code          VARCHAR(20)  NOT NULL,
    is_default_delivery  BOOLEAN      DEFAULT FALSE,
    is_default_billing   BOOLEAN      DEFAULT FALSE,
    created_at           TIMESTAMP    DEFAULT CURRENT_TIMESTAMP NULL,
    CONSTRAINT fk_address_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

-- Reviews
-- One review per user per product enforced by unique constraint.
-- Rating is an integer 1–5, validated at application level.

CREATE TABLE reviews
(
    id         VARCHAR(36)  NOT NULL PRIMARY KEY,
    product_id VARCHAR(36)  NOT NULL,
    user_id    VARCHAR(36)  NOT NULL,
    rating     INT          NOT NULL,
    title      VARCHAR(255) NULL,
    body       TEXT         NULL,
    created_at TIMESTAMP    DEFAULT CURRENT_TIMESTAMP NULL,
    CONSTRAINT uk_review_user_product UNIQUE (product_id, user_id),
    CONSTRAINT fk_review_product FOREIGN KEY (product_id) REFERENCES products (id) ON DELETE CASCADE,
    CONSTRAINT fk_review_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);
