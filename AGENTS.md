# Diretrizes do Repositório

Este arquivo contém instruções específicas para o agente Vibe. Para contribuidores humanos, consulte [README.md](./README.md) e [CONTRIBUTING.md](./CONTRIBUTING.md).

## Estilo de Código e Convenções de Nomenclatura

Siga o estilo Java já usado: indentação de quatro espaços, um tipo público por arquivo, classes em `PascalCase` e membros em `camelCase`. Nomeie as camadas de forma consistente, como `AlunoController`, `AlunoService` e `AlunoRepository`. Mantenha controllers enxutos; valide e aplique regras nos serviços, persista pelos repositórios e retorne DTOs, não entidades. Use Lombok quando os arquivos vizinhos o utilizarem e aplique `@Valid` nos DTOs de requisição.

## Diretrizes de Testes

O projeto inclui `spring-boot-starter-test`, mas ainda não possui testes versionados. Adicione testes JUnit focados, como `PlanoServiceTest`, com métodos que descrevam o comportamento esperado, por exemplo `criarRejeitaTipoDuplicado`. Execute `./mvnw test` antes de abrir uma alteração; inclua testes de regressão para correções e mudanças em regras de serviço.

## Fluxo de Trabalho

Siga as convenções de branch, commit e Pull Request definidas em [CONTRIBUTING.md](./CONTRIBUTING.md).

Não versão segredos, URLs de banco ou arquivos gerados em `target/`.
