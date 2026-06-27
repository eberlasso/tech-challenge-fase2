UPDATE tb_user_types SET name = 'RESTAURANT_OWNER' WHERE name = 'DONO_RESTAURANTE';
UPDATE tb_user_types SET name = 'CUSTOMER' WHERE name = 'CLIENTE';

INSERT INTO tb_user_types (name)
SELECT 'RESTAURANT_OWNER'
WHERE NOT EXISTS (SELECT 1 FROM tb_user_types WHERE name = 'RESTAURANT_OWNER');

INSERT INTO tb_user_types (name)
SELECT 'CUSTOMER'
WHERE NOT EXISTS (SELECT 1 FROM tb_user_types WHERE name = 'CUSTOMER');

UPDATE tb_users
SET user_type_id = (SELECT id FROM tb_user_types WHERE name = 'RESTAURANT_OWNER' LIMIT 1)
WHERE email = 'eber.l@gmail.com';

