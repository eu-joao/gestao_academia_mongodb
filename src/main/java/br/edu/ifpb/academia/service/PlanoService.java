package br.edu.ifpb.academia.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.edu.ifpb.academia.dto.Mapper;
import br.edu.ifpb.academia.dto.request.PlanoRequest;
import br.edu.ifpb.academia.dto.response.Responses.PlanoResponse;
import br.edu.ifpb.academia.entity.Plano;
import br.edu.ifpb.academia.enums.TipoPlano;
import br.edu.ifpb.academia.exception.RegraDeNegocioException;
import br.edu.ifpb.academia.exception.RecursoNaoEncontradoException;
import br.edu.ifpb.academia.repository.PlanoRepository;
import lombok.RequiredArgsConstructor;

@Service
@SuppressWarnings("null")
@RequiredArgsConstructor
public class PlanoService {

	private final PlanoRepository repository;


	@Transactional(readOnly = true)
	public List<PlanoResponse> listar() {
		return repository.findAll().stream().map(Mapper::toResponse).toList();
	}

	@Transactional(readOnly = true)
	public PlanoResponse buscar(Long id) {
		return Mapper.toResponse(buscarEntidade(id));
	}

	@Transactional
	public PlanoResponse criar(PlanoRequest request) {
		if (repository.existsByTipo(request.tipo())) {
			throw new RegraDeNegocioException("Ja existe plano deste tipo.");
		}
		Plano plano = new Plano();
		aplicar(plano, request);
		return Mapper.toResponse(repository.save(plano));
	}

	@Transactional
	public PlanoResponse atualizar(Long id, PlanoRequest request) {
		Plano plano = buscarEntidade(id);
		aplicar(plano, request);
		return Mapper.toResponse(repository.save(plano));
	}

	@Transactional
	public void excluir(Long id) {
		repository.delete(buscarEntidade(id));
	}

	public Plano buscarEntidade(Long id) {
		return repository.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Plano não encontrado com ID: " + id));
	}

	private void aplicar(Plano plano, PlanoRequest request) {
		if ((request.tipo() == TipoPlano.TRIMESTRAL || request.tipo() == TipoPlano.SEMESTRAL
				|| request.tipo() == TipoPlano.ANUAL) && BigDecimal.ZERO.compareTo(request.percentualDesconto()) >= 0) {
			throw new RegraDeNegocioException("Planos longos devem possuir desconto progressivo.");
		}
		plano.setTipo(request.tipo());
		plano.setValor(request.valor());
		plano.setDuracao(request.duracao());
		plano.setPercentualDesconto(request.percentualDesconto());
		plano.setDescricao(request.descricao());
	}
}


