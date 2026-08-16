package br.edu.ifpb.academia.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.edu.ifpb.academia.dto.Mapper;
import br.edu.ifpb.academia.dto.request.CobrancaRequest;
import br.edu.ifpb.academia.dto.request.PagamentoRequest;
import br.edu.ifpb.academia.dto.response.Responses.CobrancaResponse;
import br.edu.ifpb.academia.entity.Cobranca;
import br.edu.ifpb.academia.entity.Contrato;
import br.edu.ifpb.academia.enums.StatusCobranca;
import br.edu.ifpb.academia.exception.RecursoNaoEncontradoException;
import br.edu.ifpb.academia.exception.RegraDeNegocioException;
import br.edu.ifpb.academia.repository.CobrancaRepository;
import br.edu.ifpb.academia.repository.ContratoRepository;
import lombok.RequiredArgsConstructor;

@Service
@SuppressWarnings("null")
@RequiredArgsConstructor
public class CobrancaService {

	private static final int DIAS_CARENCIA = 2;

	private final CobrancaRepository repository;
	private final ContratoRepository contratoRepository;
	private final NotificacaoWhatsAppService notificacaoService;


	@Transactional(readOnly = true)
	public List<CobrancaResponse> listar() {
		return repository.findAll().stream().map(Mapper::toResponse).toList();
	}

	@Transactional(readOnly = true)
	public CobrancaResponse buscar(Long id) {
		return Mapper.toResponse(buscarEntidade(id));
	}

	@Transactional
	public CobrancaResponse criar(CobrancaRequest request) {
		Contrato contrato = buscarContrato(request.contratoId());
		Cobranca cobranca = new Cobranca();
		cobranca.setContrato(contrato);
		cobranca.setDataVencimento(request.dataVencimento());
		cobranca.setValor(contrato.getPlano().getValor());
		cobranca.setDataPagamento(request.dataPagamento());
		cobranca.setFormaPagamento(request.formaPagamento());
		cobranca.setStatus(request.status() == null ? StatusCobranca.PENDENTE : request.status());
		return Mapper.toResponse(repository.save(cobranca));
	}

	@Transactional
	public CobrancaResponse atualizar(Long id, CobrancaRequest request) {
		Cobranca cobranca = buscarEntidade(id);
		Contrato contrato = buscarContrato(request.contratoId());
		cobranca.setContrato(contrato);
		cobranca.setDataVencimento(request.dataVencimento());
		cobranca.setValor(contrato.getPlano().getValor());
		cobranca.setDataPagamento(request.dataPagamento());
		cobranca.setFormaPagamento(request.formaPagamento());
		cobranca.setStatus(request.status() == null ? cobranca.getStatus() : request.status());
		return Mapper.toResponse(repository.save(cobranca));
	}

	@Transactional
	public void excluir(Long id) {
		repository.delete(buscarEntidade(id));
	}

	@Transactional
	public CobrancaResponse pagar(Long id, PagamentoRequest request) {
		Cobranca cobranca = buscarEntidade(id);
		if (cobranca.isPaga()) {
			throw new RegraDeNegocioException("Esta cobrança já foi paga.");
		}
		cobranca.setDataPagamento(request.dataPagamento());
		cobranca.setFormaPagamento(request.formaPagamento());
		cobranca.setStatus(StatusCobranca.PAGO);
		return Mapper.toResponse(repository.save(cobranca));
	}

	@Transactional
	public void gerarParaContrato(Contrato contrato) {
		Cobranca cobranca = new Cobranca();
		cobranca.setContrato(contrato);
		cobranca.setDataVencimento(contrato.getDataInicio());
		cobranca.setValor(contrato.getPlano().getValor());
		cobranca.setStatus(StatusCobranca.PENDENTE);
		repository.save(cobranca);
	}

	@Transactional
	public void atualizarAtrasadas() {
		LocalDate limite = LocalDate.now().minusDays(DIAS_CARENCIA);
		List<Cobranca> vencidas = repository.findByStatusAndDataVencimentoBefore(StatusCobranca.PENDENTE, limite);
		for (Cobranca cobranca : vencidas) {
			cobranca.setStatus(StatusCobranca.ATRASADO);
			notificacaoService.cobrancaAtrasada(cobranca);
		}
		repository.saveAll(vencidas);
	}

	@Scheduled(cron = "0 0 8 * * *")
	@Transactional
	public void notificacoesAutomaticas() {
		LocalDate hoje = LocalDate.now();
		repository.findByStatusAndDataVencimentoBetween(StatusCobranca.PENDENTE, hoje, hoje.plusDays(2))
				.forEach(notificacaoService::vencimentoProximo);
		atualizarAtrasadas();
	}

	private Cobranca buscarEntidade(Long id) {
		return repository.findById(id)
				.orElseThrow(() -> new RecursoNaoEncontradoException("CobranÃ§a não encontrada com ID: " + id));
	}

	private Contrato buscarContrato(Long id) {
		return contratoRepository.findById(id)
				.orElseThrow(() -> new RecursoNaoEncontradoException("Contrato não encontrado com ID: " + id));
	}
}
