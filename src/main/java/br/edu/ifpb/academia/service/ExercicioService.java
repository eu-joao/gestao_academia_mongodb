package br.edu.ifpb.academia.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.edu.ifpb.academia.dto.Mapper;
import br.edu.ifpb.academia.dto.request.ExercicioRequest;
import br.edu.ifpb.academia.dto.response.Responses.ExercicioResponse;
import br.edu.ifpb.academia.entity.Exercicio;
import br.edu.ifpb.academia.exception.RegraDeNegocioException;
import br.edu.ifpb.academia.exception.RecursoNaoEncontradoException;
import br.edu.ifpb.academia.repository.ExercicioRepository;
import lombok.RequiredArgsConstructor;

@Service
@SuppressWarnings("null")
@RequiredArgsConstructor
public class ExercicioService {

	private final ExercicioRepository repository;


	@Transactional(readOnly = true)
	public List<ExercicioResponse> listar() {
		return repository.findAll().stream().map(Mapper::toResponse).toList();
	}

	@Transactional(readOnly = true)
	public ExercicioResponse buscar(Long id) {
		return Mapper.toResponse(buscarEntidade(id));
	}

	@Transactional
	public ExercicioResponse criar(ExercicioRequest request) {
		if (repository.existsByNome(request.nome())) {
			throw new RegraDeNegocioException("Ja existe exercicio com este nome.");
		}
		Exercicio exercicio = new Exercicio();
		aplicar(exercicio, request);
		return Mapper.toResponse(repository.save(exercicio));
	}

	@Transactional
	public ExercicioResponse atualizar(Long id, ExercicioRequest request) {
		Exercicio exercicio = buscarEntidade(id);
		aplicar(exercicio, request);
		return Mapper.toResponse(repository.save(exercicio));
	}

	@Transactional
	public void excluir(Long id) {
		repository.delete(buscarEntidade(id));
	}

	public Exercicio buscarEntidade(Long id) {
		return repository.findById(id)
				.orElseThrow(() -> new RecursoNaoEncontradoException("Exercício não encontrado com ID: " + id));
	}

	private void aplicar(Exercicio exercicio, ExercicioRequest request) {
		exercicio.setNome(request.nome());
		exercicio.setMusculaturaAlvo(request.musculaturaAlvo());
	}
}


