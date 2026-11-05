# Minimundo — Sistema de Gestão de Academia

**Instituto Federal de Educação, Ciência e Tecnologia da Paraíba**  
Campus Esperança  
Curso de Análise e Desenvolvimento de Sistemas

Disciplina: Banco de dados II
Grupo: Samuel Cláudio Duarte Lima, João Augusto Silva de Araújo, Jhonata Leonardo da silva, Miguel Rodrigo da Silva Normando

> Este documento descreve o domínio e o escopo proposto; não é uma declaração de que todos os comportamentos já foram entregues. A situação conferida no código está em [REQUISITOS.md](REQUISITOS.md). Em particular, a integração física com catraca e o envio real de WhatsApp continuam planejados.

| Área | Situação observada no código |
|---|---|
| Cadastros, planos, contratos, cobranças e operações básicas da API | Implementados, com as limitações descritas em [Arquitetura, configuração e API](ARQUITETURA_E_API.md). |
| Programas e treinos ativos | Parcial: o programa é associado ao perfil e o histórico é mantido, mas os dias e exercícios não são inicializados como fichas completas. |
| Cobranças do contrato | Parcial: a criação do contrato gera uma cobrança, não uma cobrança por todo o período contratado. |
| Catraca física e integração WhatsApp | Planejadas: há uma consulta de decisão de acesso; as mensagens de cobrança são apenas registradas no console. |

---

## Contexto e Problema

Redes de academias enfrentam dificuldades crescentes para gerenciar, de forma integrada, o controle financeiro de múltiplas unidades físicas e o acompanhamento individualizado do perfil de treino de cada aluno. A ausência de um sistema centralizado provoca inconsistências nos registros de pagamento, dificuldade no acompanhamento de inadimplência, falta de visibilidade sobre a situação financeira consolidada da rede e ausência de padronização na prescrição de treinos.

O sistema proposto tem como objetivo o gerenciamento contábil, de acesso físico e de treino de uma rede de academias. Seu escopo cobre o controle financeiro integrado à catraca de cada unidade e a geração automática de fichas de treino personalizadas por aluno, permitindo que o personal trainer tenha o programa de treino pronto no momento do cadastro. O sistema não contempla funcionalidades como agendamentos, gestão de funcionários ou modalidades online.

---

## Descrição do Domínio

### Rede de Unidades

A rede é composta por múltiplas unidades físicas, cada uma identificada por nome, endereço e CNPJ. Todas as unidades compartilham a mesma base de dados central, permitindo que um aluno matriculado em qualquer unidade tenha seus dados e situação financeira visíveis em toda a rede. A gestão é realizada por um administrador com acesso ao sistema centralizado.

### Alunos

Todo aluno é cadastrado no sistema com nome completo, CPF, telefone, e-mail e data de nascimento. O cadastro também registra informações físicas — peso e altura — e o perfil de treino: nível de experiência (Iniciante, Intermediário ou Avançado) e frequência semanal de treino disponível (3, 4, 5 ou 6 dias). Essas informações são utilizadas pelo sistema para seleção automática do programa de treino adequado ao aluno.

O aluno pertence à rede como um todo e não está vinculado a uma unidade específica em seu cadastro — a unidade de origem é registrada no contrato, permitindo que o mesmo aluno frequente diferentes unidades ao longo de sua relação com a academia. Um aluno pode estar ativo ou inativo: a inativação ocorre automaticamente quando seu contrato é cancelado ou encerrado sem renovação.

### Planos

Os planos disponíveis na rede são predefinidos e não podem ser criados de forma customizada por unidade. São cinco modalidades: diário, mensal, trimestral, semestral e anual. Os planos de maior duração oferecem desconto progressivo em relação ao valor base mensal, incentivando a fidelização do aluno. Cada plano possui um valor fixo, uma duração em dias e uma descrição. O valor da cobrança gerada para um aluno é sempre derivado diretamente do plano contratado, sem possibilidade de alteração individual por contrato.

### Programas de Treino

Os programas de treino são registros fixos que representam as combinações possíveis entre nível de experiência e frequência semanal de treino. São doze programas ao total, resultantes da combinação de três níveis com quatro frequências. Cada programa define um tipo estrutural de treinamento — Fullbody, Upper/Lower, PPL + Upper/Lower ou PPL 2x — com exercícios, séries e repetições específicos para o nível correspondente.

Programas com o mesmo tipo estrutural diferem em conteúdo: os exercícios, o volume e a intensidade variam conforme o nível de experiência, de modo que um programa Fullbody Iniciante e um Fullbody Avançado possuem exercícios distintos ainda que compartilhem a mesma estrutura semanal.

### Dias de Treino

Cada programa de treino é subdividido em dias de treino, cada um representando uma sessão com uma sequência definida de exercícios. O nome e a quantidade de dias variam conforme o tipo estrutural do programa. Os dias possuem uma ordem de execução sequencial: o aluno avança dia a dia e, ao concluir o último dia do ciclo, reinicia a sequência desde o primeiro.

### Exercícios

Os exercícios são entidades globais do sistema, existindo de forma independente de qualquer programa ou dia de treino. Cada exercício possui nome e musculatura alvo. A associação de um exercício a um dia de treino é contextual: séries, repetições e ordem de execução não são atributos do exercício em si, mas da combinação específica de dia e programa em que ele aparece. O mesmo exercício pode ter configurações distintas em programas diferentes.

### Contratos

O contrato é o vínculo formal entre o aluno e a academia. Ele registra qual plano foi escolhido, a unidade de origem, a data de início, a data de término prevista — calculada automaticamente com base na duração do plano — e o status atual (ativo ou cancelado). A unidade de origem no contrato é o único ponto de associação entre o aluno e uma unidade física, permitindo que o mesmo aluno firme contratos em unidades diferentes ao longo do tempo.

No código atual, o cancelamento de um contrato inativa o aluno, e um aluno não pode ter mais de um contrato ativo. A inativação automática por vencimento ou falta de renovação é prevista neste domínio, mas não está implementada. A tarefa diária marca uma cobrança pendente como atrasada quando sua data de vencimento é anterior a `hoje - 2 dias`; a consulta de acesso considera o status da cobrança. Não há integração física com catraca.

### Treino Ativo

O treino ativo é o vínculo entre o aluno e o programa de treino que está seguindo em determinado período. Ao ser cadastrado, o sistema identifica automaticamente o programa correspondente ao nível de experiência e à frequência semanal do aluno e cria um registro de treino ativo. Um aluno pode ter apenas um treino ativo com status ativo por vez.

Quando o nível de experiência ou a frequência semanal do aluno é atualizado, o treino ativo vigente é encerrado com registro da data de término, e um novo é criado com o programa correspondente ao novo perfil. O histórico de treinos ativos é preservado, permitindo rastrear a evolução do aluno ao longo do tempo.

### Cobranças

No modelo proposto, o contrato gera cobranças para o período contratado. No código atual, a criação do contrato gera uma única cobrança com vencimento na data de início; o valor vem do plano. Cada cobrança registra vencimento, valor, data e forma de pagamento (quando houver) e status. O pagamento é registrado por operação e cobranças pendentes passam a atrasadas depois de dois dias de carência, em uma tarefa diária.

### Controle de Acesso — requisito de catraca

O requisito prevê uma catraca por unidade e liberação com contrato ativo, sem cobranças atrasadas e respeitando a carência. Hoje existe apenas `GET /acessos/alunos/{alunoId}`, que verifica o status do aluno, contrato ativo e cobranças atrasadas. A aplicação não se comunica com equipamentos físicos.

### Notificações automáticas via WhatsApp — requisito planejado

O requisito prevê notificações para vencimento próximo e cobrança atrasada por uma API do WhatsApp. A implementação atual agenda a verificação de cobranças, mas `NotificacaoWhatsAppService` apenas escreve uma mensagem no console; não envia mensagens nem integra com serviço externo.

---

## Entidades Principais e Atributos

| Entidade | Atributos |
|---|---|
| Unidade | id, nome, endereço, CNPJ, telefone |
| Aluno | id, nome, CPF, telefone, e-mail, dataNascimento, peso, altura, nivelExperiencia (Iniciante/Intermediário/Avançado), frequenciaSemanal (3/4/5/6 dias), status (ativo/inativo) |
| Plano | id, tipo (diário/mensal/trimestral/semestral/anual), valor, duração em dias, percentual de desconto, descrição |
| ProgramaTreino | id, nivelExperiencia, frequenciaSemanal, tipoEstrutural, descrição |
| DiaTreino | id, programa, nomeDia, ordem |
| Exercicio | id, nome, musculaturaAlvo |
| DiaTreinoExercicio | id, diaTreino, exercicio, series, repeticoes, ordem |
| TreinoAtivo | id, aluno, programa, dataInicio, dataFim, status (ativo/encerrado) |
| Contrato | id, aluno, unidade, plano, dataInicio, dataTermino, status (ativo/cancelado) |
| Cobrança | id, contrato, dataVencimento, valor, dataPagamento, formaPagamento, status (pendente/pago/atrasado) |

---

## Regras de Negócio

As regras abaixo registram o domínio proposto. A situação indica o que o código atual cobre; requisitos não implementados não devem ser tratados como comportamento disponível.

| # | Regra de domínio | Situação no código |
|---|---|---|
| RN01 | Valor da cobrança sempre derivado do plano contratado; não pode ser alterado manualmente por contrato. | Implementado: criação e atualização derivam o valor do plano. |
| RN02 | Planos de maior duração possuem desconto progressivo embutido no valor total, aplicado automaticamente pelo sistema. | Parcial: há valores iniciais com descontos e validação de desconto positivo para planos longos; a progressão não é calculada automaticamente. |
| RN03 | O cancelamento de um contrato inativa automaticamente o aluno associado e bloqueia seu acesso na catraca. | Parcial: cancelar inativa o aluno e a API de acesso o nega; catraca física não está integrada. |
| RN04 | Um aluno não pode ter mais de um contrato ativo simultaneamente. | Implementado na criação do contrato. |
| RN05 | A catraca libera acesso apenas para alunos com contrato ativo e sem cobranças atrasadas; é respeitado um período de carência de dois dias após o vencimento antes do bloqueio automático. | Parcial: endpoint verifica status do aluno, contrato e cobranças; a tarefa diária usa vencimento anterior a `hoje - 2 dias`. Não há catraca física. |
| RN06 | As cobranças são geradas automaticamente no momento da criação do contrato, para todo o período contratado. | Parcial: a criação gera uma cobrança, não todas as cobranças do período. |
| RN07 | Notificações via WhatsApp são disparadas automaticamente nos eventos de vencimento próximo e cobrança atrasada, sem intervenção manual. | Parcial: a tarefa é agendada, mas o serviço apenas escreve no console e não envia WhatsApp. |
| RN08 | Um aluno só pode ter um TreinoAtivo com status ativo por vez. | Implementado pela sincronização do treino ativo; não há endpoint de encerramento manual. |
| RN09 | A seleção do ProgramaTreino é automática, determinada pela combinação de nivelExperiencia e frequenciaSemanal do aluno. | Implementado pela busca do programa correspondente ao perfil. |
| RN10 | Ao atualizar o nível de experiência ou a frequência semanal do aluno, o TreinoAtivo vigente é encerrado e um novo é criado com o programa correspondente ao novo perfil. | Implementado pela sincronização após mudança no perfil. |
| RN11 | Séries, repetições e ordem de execução são atributos de DiaTreinoExercicio — não do exercício em si. O mesmo exercício pode ter configurações distintas em programas e dias diferentes. | Implementado no modelo da entidade de associação. |
| RN12 | Os dias de treino de um programa são executados em ordem sequencial. Ao concluir o último dia do ciclo, a sequência reinicia desde o primeiro. | Planejado: não há controle de avanço ou reinício do ciclo. |
