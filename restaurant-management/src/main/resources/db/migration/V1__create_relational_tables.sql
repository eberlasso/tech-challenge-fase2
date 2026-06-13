-- 1. Tabela de Tipos de Usuário (Exigência da Fase 2)
CREATE TABLE tb_user_types (
                               id BIGSERIAL PRIMARY KEY,
                               name VARCHAR(100) NOT NULL UNIQUE
);

-- 2. Tabela de Usuários (Estrutura de suporte para associar os Donos)
CREATE TABLE tb_users (
                          id BIGSERIAL PRIMARY KEY,
                          name VARCHAR(255) NOT NULL,
                          email VARCHAR(255) NOT NULL UNIQUE,
                          password VARCHAR(255) NOT NULL,
                          user_type_id BIGINT NOT NULL,
                          CONSTRAINT fk_user_type FOREIGN KEY (user_type_id) REFERENCES tb_user_types(id)
);

-- 3. Tabela de Restaurantes (Campos obrigatórios do documento)
CREATE TABLE tb_restaurants (
                                id BIGSERIAL PRIMARY KEY,
                                name VARCHAR(255) NOT NULL,
                                address VARCHAR(255) NOT NULL,
                                cuisine_type VARCHAR(150) NOT NULL,
                                operating_hours VARCHAR(150) NOT NULL,
                                owner_id BIGINT NOT NULL,
                                CONSTRAINT fk_restaurant_owner FOREIGN KEY (owner_id) REFERENCES tb_users(id)
);

-- 4. Tabela de Itens do Cardápio (Campos obrigatórios do documento)
CREATE TABLE tb_menu_items (
                               id BIGSERIAL PRIMARY KEY,
                               name VARCHAR(255) NOT NULL,
                               description TEXT NOT NULL,
                               price NUMERIC(10, 2) NOT NULL,
                               available_only_in_restaurant BOOLEAN NOT NULL DEFAULT FALSE,
                               image_path VARCHAR(512),
                               restaurant_id BIGINT NOT NULL,
                               CONSTRAINT fk_menu_item_restaurant FOREIGN KEY (restaurant_id) REFERENCES tb_restaurants(id)
);

-- Carga inicial de Tipos de Usuário para facilitar a validação da banca
INSERT INTO tb_user_types (name) VALUES ('DONO_RESTAURANTE');
INSERT INTO tb_user_types (name) VALUES ('CLIENTE');

-- Inserção de um Usuário Administrador/Dono padrão para os testes iniciais do Postman
INSERT INTO tb_users (name, email, password, user_type_id)
VALUES ('Eber Dev', 'eber.l@gmail.com', '$2a$10$coisas_criptografadas', 1);