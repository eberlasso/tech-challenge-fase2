CREATE UNIQUE INDEX IF NOT EXISTS uq_tb_restaurants_name_address_ci
    ON tb_restaurants (name, address);

