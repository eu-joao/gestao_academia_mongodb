package br.edu.ifpb.academia.service;

import org.springframework.stereotype.Service;

import br.edu.ifpb.academia.entity.Cobranca;

@Service
@SuppressWarnings("null")
public class NotificacaoWhatsAppService {

	public void vencimentoProximo(Cobranca cobranca) {
		System.out.println("WhatsApp: vencimento proximo para " + cobranca.getContrato().getAluno().getTelefone());
	}

	public void cobrancaAtrasada(Cobranca cobranca) {
		System.out.println("WhatsApp: cobranca atrasada para " + cobranca.getContrato().getAluno().getTelefone());
	}
}

