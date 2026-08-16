package br.edu.ifpb.academia.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.edu.ifpb.academia.dto.Mapper;
import br.edu.ifpb.academia.dto.request.DiaTreinoRequest;
import br.edu.ifpb.academia.dto.response.Responses.DiaTreinoResponse;
import br.edu.ifpb.academia.entity.DiaTreino;
import br.edu.ifpb.academia.exception.RecursoNaoEncontradoException;
import br.edu.ifpb.academia.repository.DiaTreinoRepository;
import lombok.RequiredArgsConstructor;

@Service
@SuppressWarnings("null")
@RequiredArgsConstructor
public class DiaTreinoService {
	private final DiaTreinoRepository repository;
	private final ProgramaTreinoService programaTreinoService;


	@Transactional(readOnly = true)
	public List<DiaTreinoResponse> listar() {
		return repository.findAll().stream().map(Mapper::toResponse).toList();
	}

	@Transactional(readOnly = true)
	public DiaTreinoResponse buscar(Long id) {
		return Mapper.toResponse(buscarEntidade(id));
	}

	@Transactional
	public DiaTreinoResponse criar(DiaTreinoRequest request) {
		DiaTreino dia = new DiaTreino();
		aplicar(dia, request);
		return Mapper.toResponse(repository.save(dia));
	}

	@Transactional
	public DiaTreinoResponse atualizar(Long id, DiaTreinoRequest request) {
		DiaTreino dia = buscarEntidade(id);
		aplicar(dia, request);
		return Mapper.toResponse(repository.save(dia));
	}

	@Transactional
	public void excluir(Long id) {
		repository.delete(buscarEntidade(id));
	}

	public DiaTreino buscarEntidade(Long id) {
		return repository.findById(id)
				.orElseThrow(() -> new RecursoNaoEncontradoException("Dia de treino nãoo encontrado com ID: " + id));
	}

	private void aplicar(DiaTreino dia, DiaTreinoRequest request) {
		dia.setProgramaTreino(programaTreinoService.buscarEntidade(request.programaTreinoId()));
		dia.setNomeDia(request.nomeDia());
		dia.setOrdem(request.ordem());
	}
}


