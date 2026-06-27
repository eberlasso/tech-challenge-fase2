ALTER TABLE tb_users ADD COLUMN phone_number VARCHAR(20);

CREATE TABLE tb_user_roles (
    user_id BIGINT NOT NULL,
    role_name VARCHAR(100) NOT NULL,
    CONSTRAINT fk_user_roles FOREIGN KEY (user_id) REFERENCES tb_users(id),
    PRIMARY KEY (user_id, role_name)
);

