-- Flyway migration V2: translate user type names to English and ensure users reference the correct type
-- This script is safe to run idempotently: it updates existing Portuguese names to English,
-- inserts the English types when missing, and fixes the sample user to point to the
-- RESTAURANT_OWNER type.

BEGIN;

-- 1) Update existing Portuguese type names to English (preserves the same ids)
UPDATE tb_user_types SET name = 'RESTAURANT_OWNER' WHERE name = 'DONO_RESTAURANTE';
UPDATE tb_user_types SET name = 'CUSTOMER' WHERE name = 'CLIENTE';

-- 2) In case the English names don't exist (e.g. someone removed the original rows), insert them
INSERT INTO tb_user_types (name)
SELECT 'RESTAURANT_OWNER'
WHERE NOT EXISTS (SELECT 1 FROM tb_user_types WHERE name = 'RESTAURANT_OWNER');

INSERT INTO tb_user_types (name)
SELECT 'CUSTOMER'
WHERE NOT EXISTS (SELECT 1 FROM tb_user_types WHERE name = 'CUSTOMER');

-- 3) Ensure the sample user inserted by V1 references the RESTAURANT_OWNER type
UPDATE tb_users
SET user_type_id = (SELECT id FROM tb_user_types WHERE name = 'RESTAURANT_OWNER' LIMIT 1)
WHERE email = 'eber.l@gmail.com';

COMMIT;

