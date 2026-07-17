# 🍽️ Restaurant Management System — Tech Challenge Fase 2

> **FIAP Pós Tech · Fase 2** — Sistema de gestão de restaurantes com cadastro de usuários, restaurantes e cardápios, construído com Clean Architecture, Spring Boot, PostgreSQL e MongoDB.

---

## 🔗 Repositório

```
https://github.com/eberlasso/tech-challenge-fase2.git
```

---

## 🚀 Início Rápido (Para Avaliadores)

> Pré-requisito: ter **Docker** e **Docker Compose** instalados.

### Passo 1 — Clone o repositório

```bash
git clone https://github.com/eberlasso/tech-challenge-fase2.git
cd tech-challenge-fase2
```

### Passo 2 — Suba os containers com build completo (testes + cobertura)

> ⚠️ Este comando executa **todos os testes unitários e de integração** e verifica a **cobertura JaCoCo** antes de iniciar a aplicação. Aguarde a conclusão (~3 minutos).

```bash
docker compose up --build
```

Você verá no terminal a saída completa do build Maven com os testes sendo executados. Aguarde até aparecer:

```
restaurant_management_app  | Started RestaurantManagementApplication
```

### Passo 3 — (Opcional) Rodar em background

Se preferir liberar o terminal após o build, use:

```bash
# Sobe em background
docker compose up -d --build

# Acompanhe os logs em outro terminal
docker compose logs -f restaurant-app
```

Pressione `Ctrl+C` para sair dos logs (a aplicação continua rodando).

### Passo 4 — Acesse a aplicação

Quando a aplicação estiver pronta, acesse:

| Recurso | URL |
|---------|-----|
| 🌐 API Base | `http://localhost:8080/restaurant/api/v1` |
| 📖 Swagger UI | `http://localhost:8080/restaurant/swagger-ui/index.html` |
| 📋 OpenAPI JSON | `http://localhost:8080/restaurant/v3/api-docs` |

### Passo 5 — Parar o ambiente

```bash
docker compose down
```

> O arquivo `.env` já está configurado com valores padrão — **não é necessário nenhuma configuração adicional** para rodar.

---

## 📋 Índice

1. [Sobre o Projeto](#sobre-o-projeto)
2. [Arquitetura](#arquitetura)
3. [Tecnologias](#tecnologias)
4. [Estrutura de Pastas](#estrutura-de-pastas)
5. [Entidades de Domínio](#entidades-de-domínio)
6. [Endpoints da API](#endpoints-da-api)
7. [Banco de Dados](#banco-de-dados)
8. [Configuração e Execução](#configuração-e-execução)
9. [Testes](#testes)
10. [Documentação Interativa (Swagger)](#documentação-interativa-swagger)
11. [Collection Postman](#collection-postman)

---

## Sobre o Projeto

Um grupo de restaurantes da região decidiu criar um **sistema unificado de gestão**, reduzindo custos operacionais e permitindo que clientes escolham estabelecimentos com base na oferta gastronômica.

Esta é a **Fase 2** do Tech Challenge, que expande o sistema com:

- ✅ Gestão de **tipos de usuário** (`RESTAURANT_OWNER`, `CLIENT`, etc.)
- ✅ **CRUD** de cadastro de **restaurantes**
- ✅ **CRUD** de **itens do cardápio**
- ✅ **Auditoria** de operações via MongoDB
- ✅ Documentação OpenAPI / Swagger
- ✅ Infraestrutura completa com **Docker Compose**
- ✅ Cobertura de testes ≥ 95% (JaCoCo)

---

## Arquitetura

O projeto segue os princípios da **Clean Architecture**, com separação estrita de responsabilidades em camadas:

```
┌─────────────────────────────────────────────────┐
│                  Interface Adapters              │
│   (Controllers, DTOs, Mappers, OpenAPI)          │
├─────────────────────────────────────────────────┤
│              Application Business Rules          │
│          (Use Cases / Interactors)               │
├─────────────────────────────────────────────────┤
│             Enterprise Business Rules            │
│        (Entities, Gateways, Exceptions)          │
├─────────────────────────────────────────────────┤
│                  Frameworks & Drivers            │
│     (Spring Boot, JPA, MongoDB, Flyway)          │
└─────────────────────────────────────────────────┘
```

### Decisões arquiteturais importantes

| Decisão | Justificativa |
|---------|--------------|
| **Sem Lombok nas entidades de domínio** | Domínio puro, desacoplado de qualquer framework ou biblioteca externa |
| **Builder pattern manual** | Controle total sobre imutabilidade e invariantes de negócio |
| **Dual database** (PostgreSQL + MongoDB) | PostgreSQL para dados operacionais transacionais; MongoDB para logs de auditoria append-only |
| **MapStruct** | Zero boilerplate nos mapeamentos DTO ↔ domínio ↔ entidade JPA |
| **Unicidade case-insensitive** | Índices funcionais `LOWER()` no PostgreSQL (migrations V4, V5) |
| **H2 nos testes** | Testes de integração sem dependência de banco externo; MongoDB mockado via `@Primary` bean |

---

## Tecnologias

| Tecnologia | Versão | Uso |
|------------|--------|-----|
| Java | 25 | Linguagem principal |
| Spring Boot | 3.x | Framework web |
| Spring Data JPA | 3.x | Persistência relacional |
| Spring Data MongoDB | 3.x | Persistência de auditoria |
| PostgreSQL | 15.10 | Banco de dados principal |
| MongoDB | 7.0 | Banco de auditoria |
| Flyway | 10.x | Versionamento do schema |
| MapStruct | 1.6.x | Mapeamento de objetos |
| springdoc-openapi | 2.8.5 | Documentação OpenAPI 3 |
| JaCoCo | 0.8.14 | Cobertura de testes |
| JUnit 5 + Mockito | — | Testes unitários |
| H2 | — | Banco em memória para testes |
| Docker + Docker Compose | — | Infraestrutura |

---

## Estrutura de Pastas

```
tech-challenge-fase2/
├── .env                              # Variáveis de ambiente (DB/Mongo)
├── docker-compose.yml                # Orquestração dos containers
├── README.md                         # Esta documentação
└── restaurant-management/            # Módulo principal Spring Boot
    ├── Dockerfile                    # Build multi-stage
    ├── pom.xml
    └── src/
        ├── main/
        │   ├── java/com/postech/restaurantmanagement/
        │   │   ├── RestaurantManagementApplication.java
        │   │   │
        │   │   ├── domain/                          # ── CAMADA DE DOMÍNIO
        │   │   │   ├── entity/                      # Entidades de negócio (puro Java)
        │   │   │   │   ├── User.java
        │   │   │   │   ├── UserRole.java            # Enum: CLIENT, RESTAURANT_OWNER, ADMIN...
        │   │   │   │   ├── Restaurant.java
        │   │   │   │   ├── MenuItem.java
        │   │   │   │   ├── AuditLog.java
        │   │   │   │   ├── Reservation.java         # Ponto de extensão (fase futura)
        │   │   │   │   └── ReservationStatus.java
        │   │   │   ├── exception/                   # Exceções de domínio
        │   │   │   ├── gateway/                     # Interfaces de porta (contratos)
        │   │   │   └── usecase/                     # Casos de uso
        │   │   │       ├── CreateUserUseCase.java
        │   │   │       ├── CreateRestaurantUseCase.java
        │   │   │       ├── CreateMenuItemUseCase.java
        │   │   │       └── GetAuditLogsUseCase.java
        │   │   │
        │   │   └── infrastructure/                  # ── CAMADA DE INFRAESTRUTURA
        │   │       ├── config/                      # Beans Spring, OpenAPI
        │   │       ├── controller/                  # Controllers REST
        │   │       │   ├── api/                     # Interfaces OpenAPI/Swagger
        │   │       │   ├── dto/                     # Records de Request/Response
        │   │       │   └── mapper/                  # MapStruct DTO ↔ domínio
        │   │       └── persistence/                 # Adaptadores de persistência
        │   │           ├── entity/                  # Entidades JPA (PostgreSQL)
        │   │           ├── gateway/                 # Implementações dos gateways JPA
        │   │           ├── mapper/                  # MapStruct entidade ↔ domínio
        │   │           ├── repository/              # Spring Data JPA Repositories
        │   │           └── mongodb/                 # Persistência MongoDB
        │   │               ├── entity/              # @Document AuditLogEntity
        │   │               ├── gateway/             # AuditLogGatewayImpl
        │   │               └── repository/
        │   └── resources/
        │       ├── application.yaml
        │       └── db/migration/                    # Flyway V1–V5
        └── test/
            ├── java/...                             # Testes unitários e integração
            └── resources/
                ├── application-test.yaml            # Perfil H2
                └── db/migration-h2/                 # Scripts Flyway para H2
```

---

## Entidades de Domínio

> Todas as entidades são **imutáveis**, sem anotações de framework, implementadas com **Builder pattern manual**.

### User

| Campo | Tipo | Regras |
|-------|------|--------|
| `id` | `Long` | Gerado automaticamente |
| `name` | `String` | Obrigatório, não vazio |
| `email` | `String` | Obrigatório, formato válido (`^[A-Za-z0-9+_.-]+@(.+)$`) |
| `password` | `String` | Obrigatório, não vazio |
| `phoneNumber` | `String` | Opcional |
| `roles` | `Set<UserRole>` | Obrigatório, não vazio |

**`UserRole`** (enum): `CLIENT`, `RESTAURANT_OWNER`, `DELIVERY_DRIVER`, `ADMIN`, `CUSTOMER`

---

### Restaurant

| Campo | Tipo | Regras |
|-------|------|--------|
| `id` | `Long` | Gerado automaticamente |
| `name` | `String` | Obrigatório, não vazio |
| `address` | `String` | Obrigatório, não vazio |
| `cuisineType` | `String` | Obrigatório, não vazio |
| `operatingHours` | `String` | Obrigatório, não vazio |
| `owner` | `User` | Obrigatório, `owner.id` não nulo |

> Nome + endereço possuem **unicidade case-insensitive** no banco.

---

### MenuItem

| Campo | Tipo | Regras |
|-------|------|--------|
| `id` | `Long` | Gerado automaticamente |
| `name` | `String` | Obrigatório, não vazio |
| `description` | `String` | Obrigatório, não vazio |
| `price` | `BigDecimal` | Obrigatório, estritamente `> 0` |
| `availableOnlyInRestaurant` | `boolean` | `false` por padrão |
| `imagePath` | `String` | Obrigatório, não vazio (caminho do arquivo) |
| `restaurant` | `Restaurant` | Obrigatório, `restaurant.id` não nulo |

> Nome + restaurante possuem **unicidade case-insensitive** no banco.

---

### AuditLog (MongoDB)

| Campo | Tipo | Descrição |
|-------|------|-----------|
| `id` | `String` | ObjectId MongoDB |
| `action` | `String` | Tipo da ação (ex: `CREATE_USER`, `CREATE_RESTAURANT`) |
| `details` | `String` | Descrição detalhada da operação |
| `timestamp` | `LocalDateTime` | Data/hora da operação |
| `performedBy` | `String` | Identificador de quem realizou a ação |

---

## Endpoints da API

**URL base:** `http://localhost:8080/restaurant`

### 👤 Users — `POST /api/v1/users`

Cadastra um novo usuário no sistema.

**Request:**
```json
{
  "name": "João Silva",
  "email": "joao@email.com",
  "password": "StrongPass@123",
  "phoneNumber": "+55 11 98765-4321",
  "roles": ["RESTAURANT_OWNER"]
}
```

**Response `201 Created`:**
```json
{
  "id": 1,
  "name": "João Silva",
  "email": "joao@email.com",
  "phoneNumber": "+55 11 98765-4321",
  "roles": ["RESTAURANT_OWNER"]
}
```

| Código | Descrição |
|--------|-----------|
| `201` | Usuário criado com sucesso |
| `400` | Dados inválidos (email mal formatado, roles vazio, campos em branco) |
| `409` | E-mail já cadastrado |

**Outros endpoints de Users:**

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `GET` | `/api/v1/users` | Lista todos os usuários |
| `GET` | `/api/v1/users/{id}` | Busca usuário por ID |
| `PUT` | `/api/v1/users/{id}` | Atualiza usuário |
| `DELETE` | `/api/v1/users/{id}` | Remove usuário |

---

### 🍴 Restaurants — `POST /api/v1/restaurants`

Cadastra um novo restaurante associado a um usuário dono.

**Request:**
```json
{
  "name": "Pizzeria Bella Italia",
  "address": "Rua das Flores, 123, São Paulo, SP",
  "cuisineType": "Italiana",
  "operatingHours": "11:00 - 23:00",
  "ownerId": 1
}
```

**Response `201 Created`:**
```json
{
  "id": 1,
  "name": "Pizzeria Bella Italia",
  "address": "Rua das Flores, 123, São Paulo, SP",
  "cuisineType": "Italiana",
  "operatingHours": "11:00 - 23:00",
  "ownerId": 1
}
```

| Código | Descrição |
|--------|-----------|
| `201` | Restaurante criado com sucesso |
| `400` | Dados inválidos ou dono não encontrado |
| `409` | Restaurante com mesmo nome e endereço já existe |

**Outros endpoints de Restaurants:**

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `GET` | `/api/v1/restaurants` | Lista todos os restaurantes |
| `GET` | `/api/v1/restaurants/{id}` | Busca restaurante por ID |
| `PUT` | `/api/v1/restaurants/{id}` | Atualiza restaurante |
| `DELETE` | `/api/v1/restaurants/{id}` | Remove restaurante |

---

### 🍕 Menu Items — `POST /api/v1/menu-items`

Cadastra um item do cardápio para um restaurante.

**Request:**
```json
{
  "name": "Pizza Margherita",
  "description": "Pizza com molho de tomate, mozzarella e manjericão fresco",
  "price": 45.99,
  "availableOnlyInRestaurant": false,
  "imagePath": "/images/pizzas/margherita.jpg",
  "restaurantId": 1
}
```

**Response `201 Created`:**
```json
{
  "id": 1,
  "name": "Pizza Margherita",
  "description": "Pizza com molho de tomate, mozzarella e manjericão fresco",
  "price": 45.99,
  "availableOnlyInRestaurant": false,
  "imagePath": "/images/pizzas/margherita.jpg",
  "restaurantId": 1
}
```

| Código | Descrição |
|--------|-----------|
| `201` | Item criado com sucesso |
| `400` | Preço inválido (zero/negativo), restaurante não encontrado, campos em branco |
| `409` | Item com mesmo nome já existe no restaurante |

**Outros endpoints de Menu Items:**

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `GET` | `/api/v1/menu-items` | Lista todos os itens |
| `GET` | `/api/v1/menu-items/{id}` | Busca item por ID |
| `GET` | `/api/v1/menu-items?restaurantId={id}` | Lista itens por restaurante |
| `PUT` | `/api/v1/menu-items/{id}` | Atualiza item |
| `DELETE` | `/api/v1/menu-items/{id}` | Remove item |

---

### 📋 Audit Logs — `GET /api/v1/audit-logs`

Consulta os logs de auditoria armazenados no MongoDB.

**Query params (opcionais):**

| Parâmetro | Tipo | Descrição |
|-----------|------|-----------|
| `action` | `string` | Filtra pelo tipo de ação (ex: `CREATE_USER`, `CREATE_RESTAURANT`) |

**Exemplos:**
```
GET /api/v1/audit-logs
GET /api/v1/audit-logs?action=CREATE_USER
```

**Response `200 OK`:**
```json
[
  {
    "id": "507f1f77bcf86cd799439011",
    "action": "CREATE_USER",
    "details": "User joao@email.com created successfully",
    "timestamp": "2024-01-15T10:30:00",
    "performedBy": "SYSTEM"
  }
]
```

> Se `action` não for encontrado ou for vazio, retorna todos os logs (ou lista vazia `[]`).

---

### ⚠️ Formato de Erros

Todos os erros seguem o padrão do `GlobalExceptionHandler`:

```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "Descrição detalhada do erro",
  "timestamp": "2024-01-15T10:30:00",
  "path": "/api/v1/users"
}
```

---

## Banco de Dados

### PostgreSQL — Dados operacionais

Schema gerenciado pelo **Flyway** com 5 migrations:

| Migration | Descrição |
|-----------|-----------|
| `V1` | Cria tabelas `tb_user_types`, `tb_users`, `tb_restaurants`, `tb_menu_items`; seed inicial |
| `V2` | Traduz tipos de usuário para inglês (`RESTAURANT_OWNER`, `CUSTOMER`) |
| `V3` | Adiciona `phone_number` em `tb_users`; cria tabela `tb_user_roles` |
| `V4` | Índice único case-insensitive em restaurante: `LOWER(name) + LOWER(address)` |
| `V5` | Índice único case-insensitive em item de cardápio: `LOWER(name) + restaurant_id` |

**Tabelas:**

```sql
tb_user_types  (id, name UNIQUE)
tb_users       (id, name, email UNIQUE, password, phone_number, user_type_id FK)
tb_user_roles  (user_id FK, role_name) — chave composta
tb_restaurants (id, name, address, cuisine_type, operating_hours, owner_id FK)
tb_menu_items  (id, name, description, price NUMERIC(10,2),
                available_only_in_restaurant, image_path, restaurant_id FK)
```

### MongoDB — Auditoria

**Collection:** `audit_logs`

Registra automaticamente cada operação de criação via `AuditLogGatewayImpl`.

---

## Configuração e Execução

### Pré-requisitos

- **Docker** + **Docker Compose** *(recomendado)*
- *Ou:* Java 25+, Maven 3.9+, PostgreSQL 15, MongoDB 7

### Variáveis de ambiente (`.env`)

| Variável | Padrão | Descrição |
|----------|--------|-----------|
| `DB_HOST` | `localhost` | Host do PostgreSQL |
| `DB_PORT` | `5432` | Porta do PostgreSQL |
| `DB_NAME` | `db_restaurant_management` | Nome do banco |
| `DB_USER` | `postgres` | Usuário do PostgreSQL |
| `DB_PASSWORD` | *(obrigatório)* | Senha do PostgreSQL |
| `MONGO_HOST` | `localhost` | Host do MongoDB |
| `MONGO_PORT` | `27017` | Porta do MongoDB |
| `MONGO_DB_NAME` | `db_restaurant_audits` | Nome do banco MongoDB |

### 🐳 Executar com Docker

**Opção A — Ver tudo no terminal (recomendado para avaliadores)**

```bash
# Sobe com log completo do build + testes + aplicação visíveis
docker compose up --build
```

Aguarde aparecer `Started RestaurantManagementApplication` e acesse as URLs.
Pressione `Ctrl+C` para encerrar.

**Opção B — Rodar em background**

```bash
# Sobe em background
docker compose up -d --build

# Em seguida acompanhe os logs da aplicação
docker compose logs -f restaurant-app

# Para parar
docker compose down
```

**Serviços Docker Compose:**

| Serviço | Imagem | Porta | Descrição |
|---------|--------|-------|-----------|
| `restaurant-app` | Build local (`Dockerfile`) | `8080:8080` | Aplicação Spring Boot |
| `postgres-db` | `postgres:15.10` | `5432:5432` | Banco relacional |
| `mongodb` | `mongo:7.0` | `27017:27017` | Banco de auditoria |

> O `restaurant-app` aguarda o `postgres-db` estar saudável antes de iniciar (healthcheck com `pg_isready`).

### 💻 Executar localmente (sem Docker)

```bash
# 1. Garanta PostgreSQL rodando em localhost:5432
# 2. Garanta MongoDB rodando em localhost:27017
# 3. Exporte a variável de senha
export DB_PASSWORD=sua_senha

# 4. Execute a aplicação
cd restaurant-management
./mvnw spring-boot:run
```

### URLs de acesso

| Recurso | URL |
|---------|-----|
| API Base | `http://localhost:8080/restaurant/api/v1` |
| Swagger UI | `http://localhost:8080/restaurant/swagger-ui/index.html` |
| OpenAPI JSON | `http://localhost:8080/restaurant/v3/api-docs` |

---

## Testes

### Estrutura de testes

| Categoria | Classes | Descrição |
|-----------|---------|-----------|
| **Unitários — Domínio** | `UserTest`, `RestaurantTest`, `MenuItemTest`, `AuditLogTest`, `CreateUserUseCaseTest`, `CreateRestaurantUseCaseTest`, `CreateMenuItemUseCaseTest`, `GetAuditLogsUseCaseTest`, `EnumsTest`, `ExceptionsTest` | Regras de negócio puras, sem Spring |
| **Unitários — Infraestrutura** | `DtosTest`, `ControllerMappersTest`, `PersistenceMappersTest`, `GatewayImplsTest`, `AuditLogGatewayImplTest`, `ConfigurationsTest`, `GlobalExceptionHandlerIntegrationTest` | Mappers, DTOs e handlers com Mockito |
| **Integração** | `ControllersIntegrationTest` (MockMvc), `DatabaseH2IntegrationTest`, `ApiE2EH2IntegrationTest` | Fluxos HTTP completos com H2 em memória |

### Cobertura (JaCoCo)

- **Meta:** ≥ 95% de cobertura de linhas
- **Verificação:** executada automaticamente na fase `verify` do Maven
- **Exclusões:** interfaces de gateway, interfaces OpenAPI, interfaces Spring Data

### Executar os testes

```bash
cd restaurant-management

# Executa todos os testes
./mvnw test

# Executa testes + verifica cobertura JaCoCo
./mvnw verify

# Relatório de cobertura gerado em:
# target/site/jacoco/index.html
```

> ⚠️ Os testes **não precisam** de PostgreSQL ou MongoDB rodando. O perfil de testes usa **H2 em memória** e um bean mock para o MongoDB.

---

## Documentação Interativa (Swagger)

Com a aplicação rodando, acesse:

```
http://localhost:8080/restaurant/swagger-ui/index.html
```

A documentação é gerada automaticamente via **springdoc-openapi 2.8.5** com base nas interfaces anotadas (`UserApi`, `RestaurantApi`, `MenuItemApi`).

---

## Collection Postman

O arquivo `restaurant-management/postman_collection.json` contém todos os endpoints documentados com exemplos de:

- ✅ Criação bem-sucedida (`201 Created`)
- ❌ Validação inválida (`400 Bad Request`)
- ❌ Conflito de dados (`409 Conflict`)
- 📋 Consulta de logs (lista completa e filtrada por `action`)

**Como importar:**
1. Abra o Postman
2. Clique em **Import**
3. Selecione o arquivo `restaurant-management/postman_collection.json`
4. Configure a variável `baseUrl` como `http://localhost:8080/restaurant`

---

## 👥 Equipe

**FIAP Pós Tech — Turma Software Architecture**

- Maurício Borges Florencio
- Eber Lasso

---

*Tech Challenge Fase 2 — FIAP Pós Tech*
