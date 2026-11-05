# Diretrizes do Repositório

Este arquivo orienta agentes de código. Pessoas contribuidoras devem consultar [README.md](README.md) e [CONTRIBUTING.md](CONTRIBUTING.md).

## Fonte de verdade e estrutura

Confira o código e a configuração antes de documentar comportamento. A aplicação atual usa Java 17, Spring Boot, Spring Data JPA, PostgreSQL e Redis; o nome do repositório não indica o banco utilizado. A arquitetura, a configuração, as rotas e os limites das funcionalidades estão em [docs/ARQUITETURA_E_API.md](docs/ARQUITETURA_E_API.md).

O código fica em `src/main/java/br/edu/ifpb/academia/`, organizado em `config`, `controller`, `dto`, `entity`, `enums`, `exception`, `repository` e `service`; a configuração Spring está em `src/main/resources/application.properties`. Os requisitos e o minimundo descrevem também funcionalidades planejadas: consulte [docs/REQUISITOS.md](docs/REQUISITOS.md) antes de assumir que um requisito já está implementado.

## Convenções de código

Siga o estilo Java existente: indentação de quatro espaços, um tipo público por arquivo, classes em `PascalCase` e membros em `camelCase`. Mantenha controllers enxutos; valide e aplique regras nos serviços, persista pelos repositórios e retorne DTOs, não entidades. Use Lombok quando os arquivos vizinhos o utilizarem e aplique `@Valid` nos DTOs de requisição.

Não amplie regras de negócio com base apenas no minimundo. Em particular, a integração com hardware de catraca e o envio real de WhatsApp não estão implementados. Evite modificar esquema ou propor migrações em tarefas de documentação.

## Validação

Use o Maven Wrapper para compilar e executar os testes disponíveis:

```bash
./mvnw test
./mvnw clean package
```

O repositório atualmente não contém testes versionados em `src/test`. Ao alterar comportamento, adicione testes JUnit focados para a regra nova ou corrigida e execute `./mvnw test`.

## Cuidados do repositório

Siga convenções de branch, commit e Pull Request em [CONTRIBUTING.md](CONTRIBUTING.md). Não versione segredos, credenciais ou arquivos gerados em `target/`; não inclua valores reais em exemplos de configuração.
