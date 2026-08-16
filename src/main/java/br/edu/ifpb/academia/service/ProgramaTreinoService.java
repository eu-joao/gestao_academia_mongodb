package br.edu.ifpb.academia.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.edu.ifpb.academia.dto.Mapper;
import br.edu.ifpb.academia.dto.request.ProgramaTreinoRequest;
import br.edu.ifpb.academia.dto.response.Responses.ProgramaTreinoResponse;
import br.edu.ifpb.academia.entity.ProgramaTreino;
import br.edu.ifpb.academia.enums.NivelExperiencia;
import br.edu.ifpb.academia.exception.RegraDeNegocioException;
import br.edu.ifpb.academia.exception.RecursoNaoEncontradoException;
import br.edu.ifpb.academia.repository.ProgramaTreinoRepository;
import lombok.RequiredArgsConstructor;

@Service
@SuppressWarnings("null")
@RequiredArgsConstructor
public class ProgramaTreinoService {

	private final ProgramaTreinoRepository repository;


	@Transactional(readOnly = true)
	public List<ProgramaTreinoResponse> listar() {
		return repository.findAll().stream().map(Mapper::toResponse).toList();
	}

	@Transactional(readOnly = true)
	public ProgramaTreinoResponse buscar(Long id) {
		return Mapper.toResponse(buscarEntidade(id));
	}

	@Transactional
	public ProgramaTreinoResponse criar(ProgramaTreinoRequest request) {
		if (repository.existsByNivelExperienciaAndFrequenciaSemanal(request.nivelExperiencia(),
				request.frequenciaSemanal())) {
			throw new RegraDeNegocioException("Ja existe programa para este nivel e frequencia.");
		}
		ProgramaTreino programa = new ProgramaTreino();
		aplicar(programa, request);
		return Mapper.toResponse(repository.save(programa));
	}

	@Transactional
	public ProgramaTreinoResponse atualizar(Long id, ProgramaTreinoRequest request) {
		ProgramaTreino programa = buscarEntidade(id);
		aplicar(programa, request);
		return Mapper.toResponse(repository.save(programa));
	}

	@Transactional
	public void excluir(Long id) {
		repository.delete(buscarEntidade(id));
	}

	public ProgramaTreino buscarEntidade(Long id) {
		return repository.findById(id)
				.orElseThrow(() -> new RecursoNaoEncontradoException("Programa de treino nãoo encontrado com ID: " + id));
	}

	public ProgramaTreino buscarPorPerfil(NivelExperiencia nivel, Integer frequencia) {
		return repository.findByNivelExperienciaAndFrequenciaSemanal(nivel, frequencia)
				.orElseThrow(() -> new RegraDeNegocioException("Nao existe programa para o perfil do aluno."));
	}

	private void aplicar(ProgramaTreino programa, ProgramaTreinoRequest request) {
		programa.setNivelExperiencia(request.nivelExperiencia());
		programa.setFrequenciaSemanal(request.frequenciaSemanal());
		programa.setTipoEstrutural(request.tipoEstrutural());
		programa.setDescricao(request.descricao());
	}
}


