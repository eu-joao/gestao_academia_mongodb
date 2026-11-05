# Arquitetura, configuração e API

Este documento descreve o código presente no repositório, não todas as funcionalidades previstas nos requisitos.

## Arquitetura atual

A aplicação Spring Boot organiza o código por camadas no pacote `br.edu.ifpb.academia`:

- `controller`: expõe os endpoints REST e valida DTOs de entrada.
- `service`: aplica regras de negócio e coordena as operações.
- `repository`: persiste entidades com Spring Data JPA.
- `entity`: mapeia o domínio para tabelas relacionais PostgreSQL.
- `dto/request` e `dto/response`: modelos de entrada e saída; `dto/Mapper.java` faz as conversões.
- `config`: configura a conexão PostgreSQL, OpenAPI e dados iniciais.
- `exception`: centraliza exceções de domínio e respostas de erro.

O Redis é usado pelo cache Spring; a busca de plano por ID é armazenada no cache `planos`. `DataInitializer` cria cinco tipos de plano e doze combinações de nível de experiência e frequência semanal quando ainda não existem. Ele não cria dias de treino nem associa exercícios aos programas.

## Configuração

As configurações versionadas estão em [`application.properties`](../src/main/resources/application.properties). A conexão PostgreSQL é criada em [`DataSourceConfig`](../src/main/java/br/edu/ifpb/academia/config/DataSourceConfig.java) a partir da variável obrigatória `SUPABASE_DATABASE_URL`, no formato `postgresql://usuario:senha@host:porta/banco`. A porta padrão é 5432 e, se a URI não informar `sslmode`, é acrescentado `sslmode=require`. Caracteres especiais em usuário e senha devem ser codificados como componentes de URI.

| Variável/propriedade | Uso | Padrão |
|---|---|---|
| `SUPABASE_DATABASE_URL` | URI com credenciais e endereço do PostgreSQL | Obrigatória |
| `SPRING_DATA_REDIS_HOST` | Host do Redis | `localhost` |
| `SPRING_DATA_REDIS_PORT` | Porta do Redis | `6379` |
| `SERVER_PORT` | Porta HTTP da aplicação | `8080` |
| `SPRING_APPLICATION_NAME` | Nome da aplicação | `sistema-gestao-academia` |

As variáveis podem ser exportadas no terminal antes de executar `./mvnw spring-boot:run`. O Spring Boot não carrega automaticamente o arquivo `.env.example`; use-o como referência e mantenha valores e credenciais locais fora do controle de versão. Os nomes `SPRING_DATA_REDIS_HOST` e `SPRING_DATA_REDIS_PORT` correspondem às propriedades Redis configuradas pela aplicação.

O projeto inclui dependências PostgreSQL e H2, mas a configuração atual seleciona PostgreSQL. `spring.jpa.hibernate.ddl-auto=update` está configurado; esta documentação não propõe migração nem alteração do banco.

## API REST

Todos os recursos abaixo usam os caminhos relativos à raiz da aplicação. Para os recursos CRUD, `GET /recurso` lista, `GET /recurso/{id}` consulta, `POST /recurso` cria, `PUT /recurso/{id}` atualiza e `DELETE /recurso/{id}` remove. Criações retornam HTTP 201 e remoções HTTP 204.

| Recurso | Rotas e operações |
|---|---|
| Alunos | CRUD em `/alunos` |
| Unidades | CRUD em `/unidades` |
| Planos | CRUD em `/planos` |
| Exercícios | CRUD em `/exercicios` |
| Programas de treino | CRUD em `/programas-treino` |
| Dias de treino | CRUD em `/dias-treino` |
| Exercícios dos dias | CRUD em `/dias-treino-exercicios` |
| Contratos | CRUD em `/contratos`; `PUT /contratos/{id}/cancelar` cancela |
| Cobranças | CRUD em `/cobrancas`; `PUT /cobrancas/{id}/pagar` registra pagamento |
| Treinos ativos | `GET /treinos-ativos`, `GET /treinos-ativos/{id}` e `GET /treinos-ativos/aluno/{alunoId}` |
| Acesso | `GET /acessos/alunos/{alunoId}` verifica a decisão de acesso |

A interface Swagger UI está em `/swagger-ui.html`; o documento OpenAPI em `/v3/api-docs`. Ambos são configurados em [`application.properties`](../src/main/resources/application.properties).

## Regras implementadas e limites atuais

| Comportamento | Código relacionado |
|---|---|
| Cadastro de aluno rejeita CPF/e-mail duplicados; a criação e mudanças de perfil sincronizam o treino ativo. | [`AlunoService`](../src/main/java/br/edu/ifpb/academia/service/AlunoService.java), [`TreinoAtivoService`](../src/main/java/br/edu/ifpb/academia/service/TreinoAtivoService.java) |
| O programa de treino é selecionado pela combinação de nível e frequência. | [`ProgramaTreinoService`](../src/main/java/br/edu/ifpb/academia/service/ProgramaTreinoService.java) |
| Um aluno não pode ter dois contratos ativos; o cancelamento inativa o aluno e a criação do contrato gera uma cobrança. | [`ContratoService`](../src/main/java/br/edu/ifpb/academia/service/ContratoService.java), [`CobrancaService`](../src/main/java/br/edu/ifpb/academia/service/CobrancaService.java) |
| O valor de uma cobrança deriva do plano; o pagamento é registrado explicitamente. Na tarefa diária, cobranças pendentes com vencimento anterior à data de hoje menos dois dias passam a atrasadas. | [`CobrancaService`](../src/main/java/br/edu/ifpb/academia/service/CobrancaService.java) |
| A verificação de acesso considera se o aluno está ativo, possui contrato ativo e tem cobrança atrasada. | [`AcessoService`](../src/main/java/br/edu/ifpb/academia/service/AcessoService.java) |

Há limites importantes entre o requisito e o código atual:

- O contrato gera uma cobrança na criação, não cobranças para todo o período contratado.
- A atualização automática das cobranças é executada diariamente às 08:00. A consulta que marca atraso usa comparação estrita: a data de vencimento precisa ser anterior a `hoje - 2 dias`.
- `NotificacaoWhatsAppService` apenas escreve mensagens no console. Não há integração com uma API do WhatsApp.
- O endpoint de acesso fornece uma decisão à API; não há comunicação com hardware de catraca.
- O modelo e as rotas de treino existem, mas os dias e exercícios não são pré-carregados pelos dados iniciais.
- Não há testes versionados em `src/test` neste momento; `./mvnw test` executa os testes disponíveis no checkout.

Consulte [REQUISITOS.md](REQUISITOS.md) para a situação de cada requisito e [MINIMUNDO_V4.md](MINIMUNDO_V4.md) para o modelo de domínio proposto.
