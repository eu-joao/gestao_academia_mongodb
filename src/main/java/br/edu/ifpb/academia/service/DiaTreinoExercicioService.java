package br.edu.ifpb.academia.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.edu.ifpb.academia.dto.Mapper;
import br.edu.ifpb.academia.dto.request.DiaTreinoExercicioRequest;
import br.edu.ifpb.academia.dto.response.Responses.DiaTreinoExercicioResponse;
import br.edu.ifpb.academia.entity.DiaTreinoExercicio;
import br.edu.ifpb.academia.exception.RecursoNaoEncontradoException;
import br.edu.ifpb.academia.repository.DiaTreinoExercicioRepository;
import lombok.RequiredArgsConstructor;

@Service
@SuppressWarnings("null")
@RequiredArgsConstructor
public class DiaTreinoExercicioService {
	private final DiaTreinoExercicioRepository repository;
	private final DiaTreinoService diaTreinoService;
	private final ExercicioService exercicioService;


	@Transactional(readOnly = true)
	public List<DiaTreinoExercicioResponse> listar() {
		return repository.findAll().stream().map(Mapper::toResponse).toList();
	}

	@Transactional(readOnly = true)
	public DiaTreinoExercicioResponse buscar(Long id) {
		return Mapper.toResponse(buscarEntidade(id));
	}

	@Transactional
	public DiaTreinoExercicioResponse criar(DiaTreinoExercicioRequest request) {
		DiaTreinoExercicio item = new DiaTreinoExercicio();
		aplicar(item, request);
		return Mapper.toResponse(repository.save(item));
	}

	@Transactional
	public DiaTreinoExercicioResponse atualizar(Long id, DiaTreinoExercicioRequest request) {
		DiaTreinoExercicio item = buscarEntidade(id);
		aplicar(item, request);
		return Mapper.toResponse(repository.save(item));
	}

	@Transactional
	public void excluir(Long id) {
		repository.delete(buscarEntidade(id));
	}

	private DiaTreinoExercicio buscarEntidade(Long id) {
		return repository.findById(id)
				.orElseThrow(() -> new RecursoNaoEncontradoException("Vinculo não encontrado com ID: " + id));
	}

	private void aplicar(DiaTreinoExercicio item, DiaTreinoExercicioRequest request) {
		item.setDiaTreino(diaTreinoService.buscarEntidade(request.diaTreinoId()));
		item.setExercicio(exercicioService.buscarEntidade(request.exercicioId()));
		item.setSeries(request.series());
		item.setRepeticoes(request.repeticoes());
		item.setOrdem(request.ordem());
	}
}


