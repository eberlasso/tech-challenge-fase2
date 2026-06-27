-- Flyway migration V4: prevent duplicate restaurants by business identity (name + address)
-- Case-insensitive unique index keeps inserts safe even under concurrent requests.

CREATE UNIQUE INDEX IF NOT EXISTS uq_tb_restaurants_name_address_ci
    ON tb_restaurants (LOWER(name), LOWER(address));

