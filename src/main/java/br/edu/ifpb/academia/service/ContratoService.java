package br.edu.ifpb.academia.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.edu.ifpb.academia.dto.Mapper;
import br.edu.ifpb.academia.dto.request.ContratoRequest;
import br.edu.ifpb.academia.dto.response.Responses.ContratoResponse;
import br.edu.ifpb.academia.entity.Aluno;
import br.edu.ifpb.academia.entity.Contrato;
import br.edu.ifpb.academia.enums.StatusContrato;
import br.edu.ifpb.academia.exception.RecursoNaoEncontradoException;
import br.edu.ifpb.academia.exception.RegraDeNegocioException;
import br.edu.ifpb.academia.repository.ContratoRepository;
import lombok.RequiredArgsConstructor;

@Service
@SuppressWarnings("null")
@RequiredArgsConstructor
public class ContratoService {

	private final ContratoRepository repository;
	private final AlunoService alunoService;
	private final UnidadeService unidadeService;
	private final PlanoService planoService;
	private final CobrancaService cobrancaService;
	private final TreinoAtivoService treinoAtivoService;


	@Transactional(readOnly = true)
	public List<ContratoResponse> listar() {
		return repository.findAll().stream().map(Mapper::toResponse).toList();
	}

	@Transactional(readOnly = true)
	public ContratoResponse buscar(Long id) {
		return Mapper.toResponse(buscarEntidade(id));
	}

	@Transactional
	public ContratoResponse criar(ContratoRequest request) {
		if (repository.existsByAlunoIdAndStatus(request.alunoId(), StatusContrato.ATIVO)) {
			throw new RegraDeNegocioException("Aluno já possui um contrato ativo.");
		}
		Contrato contrato = new Contrato();
		aplicar(contrato, request);
		contrato = repository.save(contrato);
		cobrancaService.gerarParaContrato(contrato);
		alunoService.ativar(contrato.getAluno());
		treinoAtivoService.sincronizar(contrato.getAluno());
		return Mapper.toResponse(contrato);
	}

	@Transactional
	public ContratoResponse atualizar(Long id, ContratoRequest request) {
		Contrato contrato = buscarEntidade(id);
		if (!contrato.getAluno().getId().equals(request.alunoId())
				&& repository.existsByAlunoIdAndStatus(request.alunoId(), StatusContrato.ATIVO)) {
			throw new RegraDeNegocioException("Aluno já possui um contrato ativo.");
		}
		aplicar(contrato, request);
		return Mapper.toResponse(repository.save(contrato));
	}

	@Transactional
	public ContratoResponse cancelar(Long id) {
		Contrato contrato = buscarEntidade(id);
		if (!contrato.isAtivo()) {
			throw new RegraDeNegocioException("Este contrato já está¡ cancelado.");
		}
		contrato.setStatus(StatusContrato.CANCELADO);
		alunoService.inativar(contrato.getAluno());
		return Mapper.toResponse(repository.save(contrato));
	}

	@Transactional
	public void excluir(Long id) {
		repository.delete(buscarEntidade(id));
	}

	public Contrato buscarEntidade(Long id) {
		return repository.findById(id)
				.orElseThrow(() -> new RecursoNaoEncontradoException("Contrato não encontrado com ID: " + id));
	}

	private void aplicar(Contrato contrato, ContratoRequest request) {
		Aluno aluno = alunoService.buscarEntidade(request.alunoId());
		contrato.setAluno(aluno);
		contrato.setUnidade(unidadeService.buscarEntidade(request.unidadeId()));
		contrato.setPlano(planoService.buscarEntidade(request.planoId()));
		contrato.setDataInicio(request.dataInicio());
		contrato.setStatus(request.status() == null ? StatusContrato.ATIVO : request.status());
	}
}
