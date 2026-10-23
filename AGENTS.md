# Diretrizes do Repositório

## Estrutura do Projeto e Organização dos Módulos

Esta é uma API de gestão de academia feita com Java 17 e Spring Boot. O código da
aplicação fica em `src/main/java/br/edu/ifpb/academia`, separado por responsabilidade:

- `controller/` expõe os endpoints REST; `service/` concentra regras de negócio e transações.
- `repository/` contém o acesso com Spring Data; `entity/` mapeia o domínio.
- `dto/request/` e `dto/response/` definem a fronteira da API; `Mapper` converte entidades.
- `config/`, `exception/` e `enums/` concentram configuração, erros e valores fixos.

A configuração de execução fica em `src/main/resources/application.properties`. Requisitos do domínio e diagramas estão em `docs/`. Crie novos testes em `src/test/java`, seguindo a mesma hierarquia de pacotes.

## Comandos de Build, Teste e Desenvolvimento

Use o Maven Wrapper para não depender de uma instalação local do Maven:

```bash
./mvnw spring-boot:run  # inicia a API na porta 8080
./mvnw test             # executa a suíte de testes JUnit/Spring Boot
./mvnw clean package    # compila, testa e gera o JAR em target/
```

A interface do Swagger fica em `/swagger-ui.html`. A execução local requer PostgreSQL por meio de `SUPABASE_DATABASE_URL` e Redis em `localhost:6379`, salvo se a configuração for sobrescrita.

## Estilo de Código e Convenções de Nomenclatura

Siga o estilo Java já usado: indentação de quatro espaços, um tipo público por arquivo, classes em `PascalCase` e membros em `camelCase`. Nomeie as camadas de forma consistente, como `AlunoController`, `AlunoService` e `AlunoRepository`. Mantenha controllers enxutos; valide e aplique regras nos serviços, persista pelos repositórios e retorne DTOs, não entidades. Use Lombok quando os arquivos vizinhos o utilizarem e aplique `@Valid` nos DTOs de requisição.

## Diretrizes de Testes

O projeto inclui `spring-boot-starter-test`, mas ainda não possui testes versionados. Adicione testes JUnit focados, como `PlanoServiceTest`, com métodos que descrevam o comportamento esperado, por exemplo `criarRejeitaTipoDuplicado`. Execute `./mvnw test` antes de abrir uma alteração; inclua testes de regressão para correções e mudanças em regras de serviço.

## Commits e Pull Requests

O histórico recente usa assuntos curtos, imperativos e em português, por exemplo `Adiciona cache-aside com Redis no PlanoService`. Mantenha cada commit focado e descreva a funcionalidade alterada. Pull requests devem explicar a mudança de comportamento, vincular a issue quando existir, listar os comandos de validação e incluir exemplos de requisição/resposta ou capturas do Swagger para alterações na API. Não versione segredos, URLs de banco ou arquivos gerados em `target/`.
