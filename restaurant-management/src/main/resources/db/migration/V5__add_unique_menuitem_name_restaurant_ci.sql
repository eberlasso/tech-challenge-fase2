-- Flyway migration V5: prevent duplicate menu items by name within same restaurant
-- Case-insensitive unique index ensures menu item names are unique per restaurant.

CREATE UNIQUE INDEX IF NOT EXISTS uq_tb_menu_items_name_restaurant_ci
    ON tb_menu_items (LOWER(name), restaurant_id);

