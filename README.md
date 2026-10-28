# Gestão de Academia MongoDB

API de gestão de academia com Java 17 e Spring Boot.

## Sobre

Sistema para gerenciamento de academias, incluindo cadastro de alunos, planos, programas de treino, contratos e cobranças. O projeto segue uma arquitetura em camadas com Spring Boot e utiliza MongoDB como banco de dados.

## Estrutura

```
.
├── src/
│   ├── main/
│   │   ├── java/br/edu/ifpb/academia/
│   │   │   ├── config/          # Configurações do Spring
│   │   │   ├── controller/      # Endpoints REST
│   │   │   ├── dto/             # DTOs de requisição e resposta
│   │   │   │   ├── request/
│   │   │   │   └── response/
│   │   │   ├── entity/          # Entidades JPA
│   │   │   ├── enums/           # Valores fixos
│   │   │   ├── exception/       # Tratamento de erros
│   │   │   ├── mapper/          # Conversão entre entidades e DTOs
│   │   │   ├── repository/     # Spring Data repositories
│   │   │   └── service/         # Regras de negócio
│   │   └── resources/
│   │       └── application.properties
│   └── test/java/               # Testes
└── docs/                       # Documentação do projeto
```

## Como rodar / usar

```bash
./mvnw spring-boot:run   # Inicia a API na porta 8080
./mvnw test              # Executa os testes
./mvnw clean package     # Compila, testa e gera o JAR
```

A interface do Swagger está disponível em `/swagger-ui.html`.

Requer PostgreSQL (via `SUPABASE_DATABASE_URL`) e Redis (`localhost:6379`), a menos que sobrescrito na configuração.

## Contribuindo

Regras de branch, commit e Pull Request estão em [CONTRIBUTING.md](./CONTRIBUTING.md).
