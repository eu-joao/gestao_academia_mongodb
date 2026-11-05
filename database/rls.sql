-- =============================================================================
-- Row Level Security (RLS) — Sistema de Gestão de Academia (Supabase / PostgreSQL)
-- Issue #16
-- =============================================================================
--
-- COMO EXECUTAR
--   1. Suba a aplicação uma vez com SUPABASE_DATABASE_URL apontando para o
--      projeto Supabase: o Hibernate (ddl-auto=update) cria as tabelas.
--   2. Cole este script no SQL Editor do Supabase (ou rode com psql).
--      O script é idempotente: pode ser executado novamente sem erro.
--
-- O QUE O RLS PROTEGE
--   A API Spring conecta via JDBC com o usuário `postgres` do Supabase, que
--   ignora RLS (BYPASSRLS). Portanto este script NÃO altera o comportamento da
--   API Spring. Ele protege a porta lateral: a Data API do Supabase (PostgREST),
--   que expõe as tabelas do schema `public` aos papéis `anon` e `authenticated`.
--   Sem RLS, qualquer pessoa com a anon key leria CPF, telefone e cobranças.
--   NÃO use FORCE ROW LEVEL SECURITY: isso faria o RLS valer também para o
--   dono das tabelas e quebraria a API Spring.
--
-- PAPÉIS E MODELO DE ACESSO
--   anon                  Nenhum acesso (não há política para ele).
--   authenticated (admin) JWT com app_metadata.role = 'admin'. É o "administrador
--                         com acesso ao sistema centralizado" do minimundo.
--                         app_metadata só pode ser alterado no servidor
--                         (nunca use user_metadata para isso: o usuário edita).
--   authenticated (aluno) Qualquer outro usuário autenticado. Lê apenas os
--                         próprios dados (aluno, contratos, cobranças e treinos),
--                         identificado pelo e-mail do JWT == aluno.email,
--                         e lê o catálogo de treino. Nunca escreve.
--                         Mantenha a confirmação de e-mail ativa no Supabase Auth.
--   service_role          Ignora RLS por padrão no Supabase.
--
-- REGRAS DE NEGÓCIO REFLETIDAS
--   RF04/RF06  Planos (5) e programas de treino (12) são fixos, criados pelo
--              DataInitializer: pela Data API só leitura, nem admin altera.
--   RF03/RN03  Aluno nunca é apagado, apenas inativado: sem DELETE.
--   RN10       Histórico de treino ativo é preservado: sem DELETE.
--   RN01/RN06  Contratos e cobranças são histórico financeiro: sem DELETE
--              (cancelamento é UPDATE de status). O valor da cobrança derivar
--              do plano é regra do service, não do RLS.
--   RF08/RF09  Exercícios e sua associação a dias de treino são configuráveis
--              pelo admin, inclusive remoção.
-- =============================================================================


-- -----------------------------------------------------------------------------
-- 1. Funções auxiliares
-- -----------------------------------------------------------------------------

-- Verdadeiro quando o JWT traz app_metadata.role = 'admin'.
-- SECURITY INVOKER (padrão): lê só o JWT da requisição, sem acessar tabelas.
create or replace function public.is_admin()
returns boolean
language sql
stable
set search_path = ''
as $$
  select coalesce(auth.jwt() -> 'app_metadata' ->> 'role', '') = 'admin'
$$;

-- A função não precisa ser chamável como RPC por anônimos.
revoke execute on function public.is_admin() from public, anon;
grant execute on function public.is_admin() to authenticated;


-- -----------------------------------------------------------------------------
-- 2. Ativar RLS em todas as tabelas
-- -----------------------------------------------------------------------------
alter table public.unidade              enable row level security;
alter table public.aluno                enable row level security;
alter table public.plano                enable row level security;
alter table public.programa_treino      enable row level security;
alter table public.dia_treino           enable row level security;
alter table public.exercicio            enable row level security;
alter table public.dia_treino_exercicio enable row level security;
alter table public.contrato             enable row level security;
alter table public.cobranca             enable row level security;
alter table public.treino_ativo         enable row level security;


-- -----------------------------------------------------------------------------
-- 3. Limpeza de políticas anteriores (torna o script idempotente)
-- -----------------------------------------------------------------------------
drop policy if exists "Administradores podem gerenciar alunos" on public.aluno;
drop policy if exists "Administradores podem ver todos os alunos" on public.aluno;
drop policy if exists "Administradores podem gerenciar planos" on public.plano;
drop policy if exists "Administradores podem ver todos os planos" on public.plano;
drop policy if exists "Administradores podem gerenciar unidades" on public.unidade;
drop policy if exists "Administradores podem ver todas as unidades" on public.unidade;

drop policy if exists unidade_admin_select               on public.unidade;
drop policy if exists unidade_admin_insert               on public.unidade;
drop policy if exists unidade_admin_update               on public.unidade;

drop policy if exists aluno_admin_select                 on public.aluno;
drop policy if exists aluno_admin_insert                 on public.aluno;
drop policy if exists aluno_admin_update                 on public.aluno;
drop policy if exists aluno_proprio_select               on public.aluno;

drop policy if exists plano_select                       on public.plano;
drop policy if exists programa_treino_select             on public.programa_treino;

drop policy if exists dia_treino_select                  on public.dia_treino;
drop policy if exists dia_treino_admin_insert            on public.dia_treino;
drop policy if exists dia_treino_admin_update            on public.dia_treino;

drop policy if exists exercicio_select                   on public.exercicio;
drop policy if exists exercicio_admin_insert             on public.exercicio;
drop policy if exists exercicio_admin_update             on public.exercicio;
drop policy if exists exercicio_admin_delete             on public.exercicio;

drop policy if exists dia_treino_exercicio_select        on public.dia_treino_exercicio;
drop policy if exists dia_treino_exercicio_admin_insert  on public.dia_treino_exercicio;
drop policy if exists dia_treino_exercicio_admin_update  on public.dia_treino_exercicio;
drop policy if exists dia_treino_exercicio_admin_delete  on public.dia_treino_exercicio;

drop policy if exists contrato_admin_select              on public.contrato;
drop policy if exists contrato_admin_insert              on public.contrato;
drop policy if exists contrato_admin_update              on public.contrato;
drop policy if exists contrato_proprio_select            on public.contrato;

drop policy if exists cobranca_admin_select              on public.cobranca;
drop policy if exists cobranca_admin_insert              on public.cobranca;
drop policy if exists cobranca_admin_update              on public.cobranca;
drop policy if exists cobranca_proprio_select            on public.cobranca;

drop policy if exists treino_ativo_admin_select          on public.treino_ativo;
drop policy if exists treino_ativo_admin_insert          on public.treino_ativo;
drop policy if exists treino_ativo_admin_update          on public.treino_ativo;
drop policy if exists treino_ativo_proprio_select        on public.treino_ativo;


-- -----------------------------------------------------------------------------
-- 4. Políticas
--    (select public.is_admin()) em subselect: o Postgres avalia uma vez por
--    consulta em vez de uma vez por linha.
-- -----------------------------------------------------------------------------

-- ---- unidade (RF01): admin gerencia; sem DELETE (contratos referenciam) ------
create policy unidade_admin_select on public.unidade
  for select to authenticated
  using ((select public.is_admin()));

create policy unidade_admin_insert on public.unidade
  for insert to authenticated
  with check ((select public.is_admin()));

create policy unidade_admin_update on public.unidade
  for update to authenticated
  using ((select public.is_admin()))
  with check ((select public.is_admin()));

-- ---- aluno (RF02/RF03): admin gerencia; aluno lê só o próprio cadastro ------
create policy aluno_admin_select on public.aluno
  for select to authenticated
  using ((select public.is_admin()));

create policy aluno_admin_insert on public.aluno
  for insert to authenticated
  with check ((select public.is_admin()));

create policy aluno_admin_update on public.aluno
  for update to authenticated
  using ((select public.is_admin()))
  with check ((select public.is_admin()));

create policy aluno_proprio_select on public.aluno
  for select to authenticated
  using (lower(email) = lower(coalesce((select auth.jwt()) ->> 'email', '')));

-- ---- plano (RF04) e programa_treino (RF06): catálogo fixo, só leitura -------
create policy plano_select on public.plano
  for select to authenticated
  using (true);

create policy programa_treino_select on public.programa_treino
  for select to authenticated
  using (true);

-- ---- dia_treino (RF07): leitura para autenticados; admin organiza -----------
create policy dia_treino_select on public.dia_treino
  for select to authenticated
  using (true);

create policy dia_treino_admin_insert on public.dia_treino
  for insert to authenticated
  with check ((select public.is_admin()));

create policy dia_treino_admin_update on public.dia_treino
  for update to authenticated
  using ((select public.is_admin()))
  with check ((select public.is_admin()));

-- ---- exercicio (RF08) --------------------------------------------------------
create policy exercicio_select on public.exercicio
  for select to authenticated
  using (true);

create policy exercicio_admin_insert on public.exercicio
  for insert to authenticated
  with check ((select public.is_admin()));

create policy exercicio_admin_update on public.exercicio
  for update to authenticated
  using ((select public.is_admin()))
  with check ((select public.is_admin()));

create policy exercicio_admin_delete on public.exercicio
  for delete to authenticated
  using ((select public.is_admin()));

-- ---- dia_treino_exercicio (RF09, RN11) --------------------------------------
create policy dia_treino_exercicio_select on public.dia_treino_exercicio
  for select to authenticated
  using (true);

create policy dia_treino_exercicio_admin_insert on public.dia_treino_exercicio
  for insert to authenticated
  with check ((select public.is_admin()));

create policy dia_treino_exercicio_admin_update on public.dia_treino_exercicio
  for update to authenticated
  using ((select public.is_admin()))
  with check ((select public.is_admin()));

create policy dia_treino_exercicio_admin_delete on public.dia_treino_exercicio
  for delete to authenticated
  using ((select public.is_admin()));

-- ---- contrato (RF10/RF11, RN03/RN04): sem DELETE; cancelar = UPDATE ----------
create policy contrato_admin_select on public.contrato
  for select to authenticated
  using ((select public.is_admin()));

create policy contrato_admin_insert on public.contrato
  for insert to authenticated
  with check ((select public.is_admin()));

create policy contrato_admin_update on public.contrato
  for update to authenticated
  using ((select public.is_admin()))
  with check ((select public.is_admin()));

-- A subconsulta em aluno já passa pelo RLS dele: só enxerga a própria linha.
create policy contrato_proprio_select on public.contrato
  for select to authenticated
  using (aluno_id in (select id from public.aluno));

-- ---- cobranca (RF12/RF13, RF19): sem DELETE ---------------------------------
create policy cobranca_admin_select on public.cobranca
  for select to authenticated
  using ((select public.is_admin()));

create policy cobranca_admin_insert on public.cobranca
  for insert to authenticated
  with check ((select public.is_admin()));

create policy cobranca_admin_update on public.cobranca
  for update to authenticated
  using ((select public.is_admin()))
  with check ((select public.is_admin()));

-- A subconsulta em contrato já passa pelo RLS dele (somente contratos próprios).
create policy cobranca_proprio_select on public.cobranca
  for select to authenticated
  using (contrato_id in (select id from public.contrato));

-- ---- treino_ativo (RF14/RF15, RN08/RN10): histórico preservado, sem DELETE --
create policy treino_ativo_admin_select on public.treino_ativo
  for select to authenticated
  using ((select public.is_admin()));

create policy treino_ativo_admin_insert on public.treino_ativo
  for insert to authenticated
  with check ((select public.is_admin()));

create policy treino_ativo_admin_update on public.treino_ativo
  for update to authenticated
  using ((select public.is_admin()))
  with check ((select public.is_admin()));

create policy treino_ativo_proprio_select on public.treino_ativo
  for select to authenticated
  using (aluno_id in (select id from public.aluno));


-- -----------------------------------------------------------------------------
-- 5. Índices nas chaves estrangeiras usadas pelas políticas
--    (o Hibernate não cria índice em FK no PostgreSQL)
-- -----------------------------------------------------------------------------
create index if not exists idx_contrato_aluno_id      on public.contrato (aluno_id);
create index if not exists idx_cobranca_contrato_id   on public.cobranca (contrato_id);
create index if not exists idx_treino_ativo_aluno_id  on public.treino_ativo (aluno_id);
