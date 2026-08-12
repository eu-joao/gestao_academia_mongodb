# Documento de Requisitos 

**Projeto:** Sistema de Gestão de Academia

**Autores:** Samuel Cláudio Duarte Lima, João Augusto Silva de Araújo, Arley Andrey Alves Pereira, Miguel Rodrigo da Silva Normando

**Instituição:** Instituto Federal de Educação, Ciência e Tecnologia da Paraíba — Campus Esperança

---

## Requisitos Funcionais 

| ID | Requisito | Descrição |
|----|-----------|-----------|
| **RF01** | Gestão de Unidades | Cadastrar, editar e visualizar unidades (nome, endereço, CNPJ, telefone). |
| **RF02** | Cadastro de Alunos | Registrar dados pessoais, físicos e perfil de treino (nível e frequência). |
| **RF03** | Ativação/Inativação de Alunos | Inativar automaticamente alunos com contrato cancelado ou não renovado. |
| **RF04** | Gestão de Planos | Manter cinco planos predefinidos com valores, durações e descontos. |
| **RF05** | Seleção Automática de Programa | Atribuir programa de treino com base em nível e frequência do aluno. |
| **RF06** | Gestão de Programas de Treino | Manter doze programas fixos (3 níveis × 4 frequências) com tipos estruturais. |
| **RF07** | Gestão de Dias de Treino | Organizar dias de treino por programa com ordem sequencial. |
| **RF08** | Cadastro de Exercícios | Registrar exercícios globais (nome, musculatura alvo). |
| **RF09** | Associação Exercício-Dia-Programa | Vincular exercícios a dias de treino com séries, repetições e ordem. |
| **RF10** | Criação de Contrato | Gerar contrato (aluno, plano, unidade, datas de início e término). |
| **RF11** | Cancelamento de Contrato | Cancelar contrato e inativar aluno automaticamente. |
| **RF12** | Geração Automática de Cobranças | Criar cobranças ao firmar contrato para todo o período. |
| **RF13** | Gestão de Status de Cobrança | Atualizar status (pendente → pago/atrasado) conforme datas. |
| **RF14** | Gerenciamento de Treino Ativo | Criar, encerrar e manter histórico de treinos ativos. |
| **RF15** | Atualização de Perfil de Treino | Encerrar treino ativo e criar novo ao alterar nível ou frequência. |
| **RF16** | Controle de Acesso na Catraca | Liberar/bloquear acesso com base em contrato e cobranças. |
| **RF17** | Período de Carência | Manter acesso liberado por 2 dias após vencimento. |
| **RF18** | Notificações via WhatsApp | Enviar notificações automáticas para vencimento e atraso. |
| **RF19** | Visualização Financeira Consolidada | Exibir situação de pagamentos para toda a rede. |
| **RF20** | Geração de Ficha de Treino | Criar automaticamente programa de treino no cadastro do aluno. |

---

## Requisitos Não-Funcionais 

| ID | Requisito | Descrição |
|----|-----------|-----------|
| **RNF01** | Integração com API WhatsApp | Suportar envio automático de mensagens via WhatsApp. |
| **RNF02** | Integração com Catraca | Comunicação em tempo real com catracas das unidades. |
| **RNF03** | Base de Dados Centralizada | Compartilhamento único para todas as unidades. |
| **RNF04** | Processamento Automático | Execução automática de atualizações e notificações. |
| **RNF05** | Escalabilidade | Suportar múltiplas unidades físicas simultaneamente. |
| **RNF06** | Disponibilidade | Sistema deve funcionar 24/7 para controle de catraca. |

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
