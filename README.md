# Sistema de Gestão de Academia

API REST para gerenciar alunos, unidades, planos, contratos, cobranças e programas de treino. O nome atual do repositório contém “mongodb” por histórico; a aplicação usa PostgreSQL com JPA, não MongoDB.

## Stack e estrutura

O projeto usa Java 17, Spring Boot 3.5, Spring Data JPA, PostgreSQL e Redis para cache.

```text
src/main/java/br/edu/ifpb/academia/
├── config/       # Configurações, OpenAPI e dados iniciais
├── controller/   # Endpoints REST
├── dto/          # DTOs de requisição, resposta e mapeamento
├── entity/       # Entidades JPA
├── enums/        # Estados e tipos do domínio
├── exception/    # Exceções e tratamento de erros
├── repository/   # Repositórios Spring Data
└── service/      # Regras de negócio
src/main/resources/application.properties
src/test/         # Testes automatizados, quando existentes
docs/             # Requisitos, minimundo e diagramas
database/         # Scripts SQL
```

## Configuração e execução

Pré-requisitos: JDK 17, PostgreSQL e Redis. A aplicação exige `SUPABASE_DATABASE_URL` com uma URI PostgreSQL completa, incluindo usuário, senha, host e banco. Exemplo de formato:

```text
postgresql://<usuario>:<senha>@<host>:5432/<banco>?sslmode=require
```

O padrão de conexão adiciona `sslmode=require` quando a URI não especifica um `sslmode`. Configure as variáveis no ambiente antes de iniciar (credenciais não devem ser versionadas):

```bash
export SUPABASE_DATABASE_URL='postgresql://<usuario>:<senha>@<host>:5432/<banco>?sslmode=require'
export SPRING_DATA_REDIS_HOST=localhost
export SPRING_DATA_REDIS_PORT=6379
./mvnw spring-boot:run
```

O Redis usa `localhost:6379` por padrão e a API usa a porta `8080`; ambas podem ser sobrescritas pelas propriedades Spring correspondentes, como `SPRING_DATA_REDIS_HOST`, `SPRING_DATA_REDIS_PORT` e `SERVER_PORT`. O arquivo `.env.example` serve como referência, mas suas variáveis precisam ser carregadas/exportadas no ambiente do processo.

```bash
./mvnw test           # Executa os testes disponíveis
./mvnw clean package  # Compila, testa e gera o JAR
```

## GitHub Codespaces

O ambiente do Codespaces usa Java 17 e inicia um serviço Redis junto com o workspace. Nele, a aplicação acessa o Redis pelo host `redis`; fora do Codespaces, o padrão continua sendo `localhost:6379`.

Antes de iniciar a API, configure `SUPABASE_DATABASE_URL` como um secret do Codespaces com acesso a este repositório. O secret deve conter a URI de conexão do PostgreSQL do Supabase. Não salve esse valor em arquivos do repositório. Se o Codespace já estiver aberto quando o secret for criado ou alterado, reinicie-o para disponibilizar a variável de ambiente.

No terminal do Codespace:

```bash
./mvnw spring-boot:run
```

A porta 8080 da API é encaminhada pelo Codespaces. Para executar os testes:

```bash
./mvnw test
```

## API e documentação

Com a aplicação em execução, consulte a interface Swagger em [`/swagger-ui.html`](http://localhost:8080/swagger-ui.html) e o documento OpenAPI em [`/v3/api-docs`](http://localhost:8080/v3/api-docs). A arquitetura, a configuração detalhada e as rotas estão em [docs/ARQUITETURA_E_API.md](docs/ARQUITETURA_E_API.md).

Os [requisitos](docs/REQUISITOS.md) e o [minimundo](docs/MINIMUNDO_V4.md) descrevem o escopo do domínio e indicam o que está implementado, parcial ou ainda planejado. Diagramas e outros documentos estão em [docs/](docs/).

Para contribuir, consulte [CONTRIBUTING.md](CONTRIBUTING.md). Orientações específicas para agentes de código estão em [AGENTS.md](AGENTS.md).
