CREATE UNIQUE INDEX IF NOT EXISTS uq_tb_menu_items_name_restaurant_ci
    ON tb_menu_items (name, restaurant_id);

