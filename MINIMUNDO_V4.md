# Minimundo — Sistema de Gestão de Academia

**Instituto Federal de Educação, Ciência e Tecnologia da Paraíba**  
Campus Esperança  
Curso de Análise e Desenvolvimento de Sistemas

Disciplina: Desenvolvimento de Aplicações Web II  
Grupo: Samuel Cláudio Duarte Lima, João Augusto Silva de Araújo, Arley Andrey Alves Pereira, Miguel Rodrigo da Silva Normando

---

## Contexto e Problema

Redes de academias de ginástica enfrentam dificuldades crescentes para gerenciar, de forma integrada, o controle financeiro de múltiplas unidades físicas e o acompanhamento individualizado do perfil de treino de cada aluno. A ausência de um sistema centralizado provoca inconsistências nos registros de pagamento, dificuldade no acompanhamento de inadimplência, falta de visibilidade sobre a situação financeira consolidada da rede e ausência de padronização na prescrição de treinos.

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

Quando um contrato é cancelado, o aluno associado é automaticamente marcado como inativo, tendo seu acesso físico bloqueado na catraca. Um aluno pode ter apenas um contrato ativo por vez. O sistema prevê um período de carência de dois dias após o vencimento da cobrança, durante o qual o acesso permanece liberado mesmo sem confirmação do pagamento. Após esse prazo, o bloqueio é aplicado automaticamente.

### Treino Ativo

O treino ativo é o vínculo entre o aluno e o programa de treino que está seguindo em determinado período. Ao ser cadastrado, o sistema identifica automaticamente o programa correspondente ao nível de experiência e à frequência semanal do aluno e cria um registro de treino ativo. Um aluno pode ter apenas um treino ativo com status ativo por vez.

Quando o nível de experiência ou a frequência semanal do aluno é atualizado, o treino ativo vigente é encerrado com registro da data de término, e um novo é criado com o programa correspondente ao novo perfil. O histórico de treinos ativos é preservado, permitindo rastrear a evolução do aluno ao longo do tempo.

### Cobranças

Ao ser firmado um contrato, o sistema gera automaticamente as cobranças correspondentes ao período contratado, com base no valor e na duração do plano. Cada cobrança registra a data de vencimento, o valor, a data de pagamento (quando houver), a forma de pagamento utilizada e o status (pendente, pago ou atrasado). O status de cada cobrança é atualizado automaticamente pelo sistema conforme as datas de vencimento são atingidas.

### Controle de Acesso — Catraca

Cada unidade da rede possui uma catraca integrada ao sistema. O acesso do aluno é liberado automaticamente quando há contrato ativo e nenhuma cobrança atrasada em aberto, respeitado o período de carência. Caso o aluno esteja com pagamento em atraso ou com contrato cancelado, a catraca é bloqueada automaticamente até que a situação seja regularizada.

### Notificações Automáticas via WhatsApp

O sistema realiza o envio automático de notificações para o número de telefone cadastrado do aluno por meio da API do WhatsApp. As notificações são disparadas nos seguintes eventos: proximidade do vencimento de uma cobrança e registro de cobrança em atraso. O envio é automático, sem necessidade de intervenção manual do administrador.

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

| # | Regra |
|---|---|
| RN01 | Valor da cobrança sempre derivado do plano contratado; não pode ser alterado manualmente por contrato. |
| RN02 | Planos de maior duração possuem desconto progressivo embutido no valor total, aplicado automaticamente pelo sistema. |
| RN03 | O cancelamento de um contrato inativa automaticamente o aluno associado e bloqueia seu acesso na catraca. |
| RN04 | Um aluno não pode ter mais de um contrato ativo simultaneamente. |
| RN05 | A catraca libera acesso apenas para alunos com contrato ativo e sem cobranças atrasadas; é respeitado um período de carência de dois dias após o vencimento antes do bloqueio automático. |
| RN06 | As cobranças são geradas automaticamente no momento da criação do contrato, para todo o período contratado. |
| RN07 | Notificações via WhatsApp são disparadas automaticamente nos eventos de vencimento próximo e cobrança atrasada, sem intervenção manual. |
| RN08 | Um aluno só pode ter um TreinoAtivo com status ativo por vez. |
| RN09 | A seleção do ProgramaTreino é automática, determinada pela combinação de nivelExperiencia e frequenciaSemanal do aluno. |
| RN10 | Ao atualizar o nível de experiência ou a frequência semanal do aluno, o TreinoAtivo vigente é encerrado e um novo é criado com o programa correspondente ao novo perfil. |
| RN11 | Séries, repetições e ordem de execução são atributos de DiaTreinoExercicio — não do exercício em si. O mesmo exercício pode ter configurações distintas em programas e dias diferentes. |
| RN12 | Os dias de treino de um programa são executados em ordem sequencial. Ao concluir o último dia do ciclo, a sequência reinicia desde o primeiro. |
