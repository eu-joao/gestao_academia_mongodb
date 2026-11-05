# Documento de Requisitos

Este documento registra o escopo do domínio. Os requisitos não são, por si só, evidência de funcionalidade entregue. A coluna “Situação no código” foi conferida com a implementação atual; detalhes e arquivos relacionados estão em [Arquitetura, configuração e API](ARQUITETURA_E_API.md).

**Projeto:** Sistema de Gestão de Academia

**Autores:** Samuel Cláudio Duarte Lima, João Augusto Silva de Araújo, Arley Andrey Alves Pereira, Miguel Rodrigo da Silva Normando

**Instituição:** Instituto Federal de Educação, Ciência e Tecnologia da Paraíba — Campus Esperança

---

## Requisitos Funcionais 

| ID | Requisito | Descrição | Situação no código |
|----|-----------|-----------|--------------------|
| **RF01** | Gestão de Unidades | Cadastrar, editar e visualizar unidades (nome, endereço, CNPJ, telefone). | Implementado: CRUD de unidades. |
| **RF02** | Cadastro de Alunos | Registrar dados pessoais, físicos e perfil de treino (nível e frequência). | Implementado: CRUD de alunos e sincronização do treino na criação. |
| **RF03** | Ativação/Inativação de Alunos | Inativar automaticamente alunos com contrato cancelado ou não renovado. | Parcial: cancelamento inativa; vencimento/não renovação não inativa automaticamente. |
| **RF04** | Gestão de Planos | Manter cinco planos predefinidos com valores, durações e descontos. | Implementado: cinco tipos são inicializados; também há CRUD. |
| **RF05** | Seleção Automática de Programa | Atribuir programa de treino com base em nível e frequência do aluno. | Implementado: seleção por nível e frequência. |
| **RF06** | Gestão de Programas de Treino | Manter doze programas fixos (3 níveis × 4 frequências) com tipos estruturais. | Parcial: doze combinações são inicializadas; dias e exercícios não são pré-carregados. |
| **RF07** | Gestão de Dias de Treino | Organizar dias de treino por programa com ordem sequencial. | Parcial: dias e ordem podem ser cadastrados; não há avanço automático pelo ciclo. |
| **RF08** | Cadastro de Exercícios | Registrar exercícios globais (nome, musculatura alvo). | Implementado: CRUD de exercícios. |
| **RF09** | Associação Exercício-Dia-Programa | Vincular exercícios a dias de treino com séries, repetições e ordem. | Implementado: associação gerenciada pelo recurso de exercícios dos dias. |
| **RF10** | Criação de Contrato | Gerar contrato (aluno, plano, unidade, datas de início e término). | Implementado: término é calculado a partir da duração do plano. |
| **RF11** | Cancelamento de Contrato | Cancelar contrato e inativar aluno automaticamente. | Implementado: o cancelamento atualiza o contrato e inativa o aluno. |
| **RF12** | Geração Automática de Cobranças | Criar cobranças ao firmar contrato para todo o período. | Parcial: uma cobrança é criada por contrato, não uma para cada período. |
| **RF13** | Gestão de Status de Cobrança | Atualizar status (pendente → pago/atrasado) conforme datas. | Parcial: pagamento é registrado por operação; cobranças pendentes atrasam após a carência. |
| **RF14** | Gerenciamento de Treino Ativo | Criar, encerrar e manter histórico de treinos ativos. | Parcial: criação, sincronização e consulta do histórico existem; não há operação dedicada para encerrar manualmente. |
| **RF15** | Atualização de Perfil de Treino | Encerrar treino ativo e criar novo ao alterar nível ou frequência. | Implementado: a mudança de perfil sincroniza o treino ativo. |
| **RF16** | Controle de Acesso na Catraca | Liberar/bloquear acesso com base em contrato e cobranças. | Parcial: há decisão por endpoint; integração física com catraca não existe. |
| **RF17** | Período de Carência | Manter acesso liberado por 2 dias após vencimento. | Parcial: a tarefa diária marca como atrasada a cobrança pendente com vencimento anterior a `hoje - 2 dias`; a consulta de acesso usa esse status, mas não há catraca física. |
| **RF18** | Notificações via WhatsApp | Enviar notificações automáticas para vencimento e atraso. | Planejado: o serviço atual apenas escreve mensagens no console; não chama API externa. |
| **RF19** | Visualização Financeira Consolidada | Exibir situação de pagamentos para toda a rede. | Planejado: não há endpoint ou relatório consolidado específico. |
| **RF20** | Geração de Ficha de Treino | Criar automaticamente programa de treino no cadastro do aluno. | Parcial: o vínculo com programa é criado; os treinos/dias/exercícios não são gerados ou semeados como ficha completa. |

---

## Requisitos Não-Funcionais 

| ID | Requisito | Descrição | Situação no código |
|----|-----------|-----------|--------------------|
| **RNF01** | Integração com API WhatsApp | Suportar envio automático de mensagens via WhatsApp. | Planejado: não há integração externa implementada. |
| **RNF02** | Integração com Catraca | Comunicação em tempo real com catracas das unidades. | Planejado: existe somente a consulta de acesso pela API. |
| **RNF03** | Base de Dados Centralizada | Compartilhamento único para todas as unidades. | Parcial: a aplicação conecta a um PostgreSQL; operação em rede/múltiplas unidades não foi validada aqui. |
| **RNF04** | Processamento Automático | Execução automática de atualizações e notificações. | Parcial: atualização de cobranças/notificações está agendada; mensagens WhatsApp reais não são enviadas. |
| **RNF05** | Escalabilidade | Suportar múltiplas unidades físicas simultaneamente. | Planejado: não há evidência de validação de escala no código/documentação. |
| **RNF06** | Disponibilidade | Sistema deve funcionar 24/7 para controle de catraca. | Planejado: disponibilidade operacional e catraca integrada não estão implementadas/validadas. |

---

## Glossário 

| Termo | Definição |
|-------|-----------|
| **Nível de Experiência** | Classificação do aluno: Iniciante, Intermediário ou Avançado. |
| **Frequência Semanal** | Número de dias por semana que o aluno pode treinar (3, 4, 5 ou 6). |
| **Tipo Estrutural** | Modelo de programa de treino: Fullbody, Upper/Lower, PPL + Upper/Lower ou PPL 2x. |
| **Carência** | Período de 2 dias após vencimento da cobrança em que o acesso permanece liberado. |
| **Treino Ativo** | Vínculo atual entre aluno e programa de treino. |
| **Catraca** | Equipamento de controle de acesso físico integrado ao sistema. |
