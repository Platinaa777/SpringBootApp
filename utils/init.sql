create table if not exists clients
(
    id bigserial primary key,
    email text not null unique,
    password text not null,
    role text default ('Client')
);

create table if not exists transactions
(
    id text not null primary key,
    earnings bigserial constraint earnings_check check (earnings >= 0),
    status text default 'IN_PROGRESS'::text,
    client_email text not null
);

create table if not exists dishes
(
    id bigserial primary key,
    title text not null,
    price bigserial constraint dishes_price_check check (price > 0),
    amount bigserial constraint dishes_amount_check check (amount >= 0),
    duration_seconds bigserial constraint dishes_duration_seconds_check check (duration_seconds > 0)
);

create table if not exists orders
(
    id bigserial primary key,
    transaction_id text not null,
    started_at timestamp default CURRENT_TIMESTAMP,
    finished_at timestamp not null
);

create table if not exists dish_order
(
    id bigserial primary key,
    dish_id bigserial,
    order_id bigserial,
    dish_amount bigserial
);