-- Flyway migration V3: Add missing phone_number and user_roles table to align with entity mappings
-- This migration aligns the database schema with the User entity which has phoneNumber field

-- 1. Add phone_number column to tb_users (nullable initially to support existing rows)
ALTER TABLE tb_users ADD COLUMN phone_number VARCHAR(20);

-- 2. Create tb_user_roles table for many-to-many user-role relationship
CREATE TABLE tb_user_roles (
    user_id BIGINT NOT NULL,
    role_name VARCHAR(100) NOT NULL,
    CONSTRAINT fk_user_roles FOREIGN KEY (user_id) REFERENCES tb_users(id),
    PRIMARY KEY (user_id, role_name)
);


