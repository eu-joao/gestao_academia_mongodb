package br.edu.ifpb.academia.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.edu.ifpb.academia.dto.Mapper;
import br.edu.ifpb.academia.dto.request.UnidadeRequest;
import br.edu.ifpb.academia.dto.response.Responses.UnidadeResponse;
import br.edu.ifpb.academia.entity.Unidade;
import br.edu.ifpb.academia.exception.RegraDeNegocioException;
import br.edu.ifpb.academia.exception.RecursoNaoEncontradoException;
import br.edu.ifpb.academia.repository.UnidadeRepository;
import lombok.RequiredArgsConstructor;

@Service
@SuppressWarnings("null")
@RequiredArgsConstructor
public class UnidadeService {

	private final UnidadeRepository repository;


	@Transactional(readOnly = true)
	public List<UnidadeResponse> listar() {
		return repository.findAll().stream().map(Mapper::toResponse).toList();
	}

	@Transactional(readOnly = true)
	public UnidadeResponse buscar(Long id) {
		return Mapper.toResponse(buscarEntidade(id));
	}

	@Transactional
	public UnidadeResponse criar(UnidadeRequest request) {
		if (repository.existsByCnpj(request.cnpj())) {
			throw new RegraDeNegocioException("Ja existe unidade com este CNPJ.");
		}
		Unidade unidade = new Unidade();
		aplicar(unidade, request);
		return Mapper.toResponse(repository.save(unidade));
	}

	@Transactional
	public UnidadeResponse atualizar(Long id, UnidadeRequest request) {
		Unidade unidade = buscarEntidade(id);
		aplicar(unidade, request);
		return Mapper.toResponse(repository.save(unidade));
	}

	@Transactional
	public void excluir(Long id) {
		repository.delete(buscarEntidade(id));
	}

	public Unidade buscarEntidade(Long id) {
		return repository.findById(id)
				.orElseThrow(() -> new RecursoNaoEncontradoException("Unidade não encontrada com ID: " + id));
	}

	private void aplicar(Unidade unidade, UnidadeRequest request) {
		unidade.setNome(request.nome());
		unidade.setEndereco(request.endereco());
		unidade.setCnpj(request.cnpj());
		unidade.setTelefone(request.telefone());
	}
}


