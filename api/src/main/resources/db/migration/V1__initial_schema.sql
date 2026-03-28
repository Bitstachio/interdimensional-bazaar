create table categories
(
    id          varchar(36)  not null
        primary key,
    name        varchar(100) not null,
    slug        varchar(100) not null,
    description text null,
    created_at  timestamp default CURRENT_TIMESTAMP null,
    constraint name
        unique (name),
    constraint slug
        unique (slug)
);

create table products
(
    id             varchar(36)    not null
        primary key,
    category_id    varchar(36) null,
    name           varchar(255)   not null,
    slug           varchar(255)   not null,
    description    text null,
    price          decimal(10, 2) not null,
    stock_quantity int       default 0 null,
    image_url      varchar(512) null,
    is_active      tinyint(1) default 1                 null,
    created_at     timestamp default CURRENT_TIMESTAMP null,
    updated_at     timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    constraint slug
        unique (slug),
    constraint fk_product_category
        foreign key (category_id) references categories (id)
            on delete set null
);

create table users
(
    id            varchar(36)  not null
        primary key,
    first_name    varchar(50) null,
    last_name     varchar(50) null,
    email         varchar(191) not null,
    password_hash varchar(255) not null,
    created_at    timestamp default CURRENT_TIMESTAMP null,
    constraint email
        unique (email)
);

create table carts
(
    id         varchar(36) not null
        primary key,
    user_id    varchar(36) null,
    created_at timestamp default CURRENT_TIMESTAMP null,
    constraint user_id
        unique (user_id),
    constraint fk_cart_user
        foreign key (user_id) references users (id)
            on delete cascade
);

create table cart_items
(
    id         bigint auto_increment
        primary key,
    cart_id    varchar(36)   not null,
    product_id varchar(36)   not null,
    quantity   int default 1 not null,
    constraint uk_cart_product
        unique (cart_id, product_id),
    constraint fk_items_cart
        foreign key (cart_id) references carts (id)
            on delete cascade,
    constraint fk_items_product
        foreign key (product_id) references products (id)
            on delete cascade
);

