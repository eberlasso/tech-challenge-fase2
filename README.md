# tech-challenge-fase2

Projeto da Pós Tech FIAP para gestão de usuários, restaurantes e cardápios.

## Executar com Docker (Ubuntu Server)

### 1) Clonar o projeto

```bash
git clone https://github.com/eberlasso/tech-challenge-fase2.git
cd tech-challenge-fase2
```

### 2) Ajustar variáveis de ambiente

Edite o arquivo `.env` na raiz do projeto e defina ao menos uma senha forte para `DB_PASSWORD`.

### 3) Build e start dos containers

```bash
docker compose up -d --build
```

### 4) Verificar logs da aplicação

```bash
docker compose logs -f restaurant-app
```

### 5) Parar o ambiente

```bash
docker compose down
```

## Endpoints locais

- API base: `http://localhost:8080/restaurant`
- Swagger UI: `http://localhost:8080/restaurant/swagger-ui/index.html`

## Serviços do Docker Compose

- `restaurant-app`: aplicação Spring Boot
- `postgres-db`: banco transacional PostgreSQL
- `mongodb`: banco de auditoria
