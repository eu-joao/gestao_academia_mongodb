# Contribuindo

## Sumário

- [Branches](#branches)
- [Commits](#commits)
- [Pull Requests](#pull-requests)

## Branches

Use o formato `<número-issue>-<slug>` para nomear branches. Exemplo: `12-adicionar-diretrizes-para-contribuidores`.

## Commits

Siga o **Conventional Commits** adaptado para português. O formato é:
```
<tipo>(<escopo opcional>): <descrição em imperativo, minúsculas, sem ponto final>
```

**Tipos principais:**
- `feat` — nova funcionalidade
- `fix` — correção de bug
- `docs` — documentação
- `test` — testes
- `refactor` — refatoração (sem mudança de funcionalidade)
- `style` — formatação, lint, etc
- `chore` — tarefas de manutenção (dependências, configuração)

**Exemplos:**
- `feat: adiciona cache-aside com Redis no PlanoService`
- `fix: corrigir validação de email no AlunoService`
- `docs: atualiza DIAGRAMAS.md com modelo lógico`
- `test: adiciona testes de regressão para PlanoController`

Sempre inclua co-autoria quando houver colaboração:
```
feat: adiciona cache-aside com Redis no PlanoService

Co-authored-by: João Silva <joao@example.com>
Co-authored-by: Mistral Vibe <vibe@mistral.ai>
```

## Pull Requests

A descrição de um PR deve conter:

- **O que mudou?** — 1-3 frases explicando a mudança de comportamento
- **Issue:** — link ou número da issue que o PR fecha
- **Comandos de validação:** — ex: `./mvnw test`
- **Exemplos:** — requisição/resposta ou capturas do Swagger para alterações na API

Mantenha cada commit **focado** (uma mudança lógica) e em **português**. Descreva a funcionalidade alterada de forma clara.
