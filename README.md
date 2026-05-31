# Barraca Tipo — Sistema Web

Migração do projeto console para uma aplicação web com **Spring Boot** no backend e **Angular** no frontend.

## Integrantes

- Estenio Gabriel
- Gabriel Brandão
- Guilherme

---

## Pré-requisitos

| Ferramenta | Versão mínima |
|---|---|
| Java JDK | 17 |
| Maven | 3.9+ (ou usar o `mvnw` incluso) |
| Node.js | 18 LTS |
| Angular CLI | 17+ (`npm install -g @angular/cli`) |

---

## Rodando o Backend (Spring Boot)

```bash
cd backend
./mvnw spring-boot:run
```

O servidor sobe em **http://localhost:8080**.

Console H2 disponível em **http://localhost:8080/h2-console**
- JDBC URL: `jdbc:h2:mem:barracadb`
- Usuário: `sa` / Senha: *(vazio)*

---

## Rodando o Frontend (Angular)

```bash
cd frontend
npm install
ng serve
```

A aplicação abre em **http://localhost:4200**.

---

## Principais Endpoints da API

### Tipos de Barraca

| Método | Rota | Descrição |
|---|---|---|
| GET | `/api/tipos-barraca` | Lista todos os tipos |
| GET | `/api/tipos-barraca/{id}` | Busca por ID |
| POST | `/api/tipos-barraca` | Cadastra novo tipo |
| DELETE | `/api/tipos-barraca/{id}` | Remove um tipo |

### Barracas

| Método | Rota | Descrição |
|---|---|---|
| GET | `/api/barracas` | Lista todas as barracas |
| GET | `/api/barracas/{id}` | Busca por ID |
| POST | `/api/barracas` | Cadastra nova barraca |
| PUT | `/api/barracas/{id}` | Atualiza uma barraca |
| DELETE | `/api/barracas/{id}` | Remove uma barraca |

---

## Estrutura do Projeto

```
Barraca-Tipo/
├── backend/                        # API REST — Spring Boot
│   └── src/main/java/com/barraca/backend/
│       ├── config/                 # CorsConfig
│       ├── controller/             # TipoBarracaController, BarracaController
│       ├── domain/                 # Entidades JPA (TipoBarraca, Barraca)
│       ├── dto/                    # DTOs de entrada e saída
│       ├── exception/              # RegraNegocioException, GlobalExceptionHandler
│       ├── repository/             # Interfaces JpaRepository
│       └── service/                # Regras de negócio
└── frontend/                       # SPA — Angular
    └── src/app/
        ├── components/
        │   ├── barraca-list/       # Tela de listagem
        │   └── barraca-form/       # Tela de cadastro/edição
        ├── models/                 # Interfaces TypeScript
        └── services/               # Chamadas HTTP à API
```

---

## Respostas de Erro da API

**HTTP 400 — Dados inválidos (Bean Validation):**
```json
{
  "status": 400,
  "mensagem": "Dados inválidos",
  "erros": ["O nome é obrigatório", "O tipo de barraca é obrigatório"]
}
```

**HTTP 422 — Regra de negócio violada:**
```json
{
  "status": 422,
  "mensagem": "Já existe uma Barraca com o nome: Barraca Central"
}
```
