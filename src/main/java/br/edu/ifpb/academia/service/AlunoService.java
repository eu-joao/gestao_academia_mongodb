package br.edu.ifpb.academia.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.edu.ifpb.academia.dto.Mapper;
import br.edu.ifpb.academia.dto.request.AlunoRequest;
import br.edu.ifpb.academia.dto.response.Responses.AlunoResponse;
import br.edu.ifpb.academia.entity.Aluno;
import br.edu.ifpb.academia.enums.StatusAluno;
import br.edu.ifpb.academia.exception.RegraDeNegocioException;
import br.edu.ifpb.academia.exception.RecursoNaoEncontradoException;
import br.edu.ifpb.academia.repository.AlunoRepository;
import lombok.RequiredArgsConstructor;

@Service
@SuppressWarnings("null")
@RequiredArgsConstructor
public class AlunoService {

	private final AlunoRepository repository;
	private final TreinoAtivoService treinoAtivoService;


	@Transactional(readOnly = true)
	public List<AlunoResponse> listar() {
		return repository.findAll().stream().map(Mapper::toResponse).toList();
	}

	@Transactional(readOnly = true)
	public AlunoResponse buscar(Long id) {
		return Mapper.toResponse(buscarEntidade(id));
	}

	@Transactional
	public AlunoResponse criar(AlunoRequest request) {
		if (repository.existsByCpf(request.cpf())) {
			throw new RegraDeNegocioException("Ja existe aluno com este CPF.");
		}
		if (repository.existsByEmail(request.email())) {
			throw new RegraDeNegocioException("Ja existe aluno com este e-mail.");
		}
		Aluno aluno = new Aluno();
		aplicar(aluno, request);
		aluno = repository.save(aluno);
		treinoAtivoService.sincronizar(aluno);
		return Mapper.toResponse(aluno);
	}

	@Transactional
	public AlunoResponse atualizar(Long id, AlunoRequest request) {
		Aluno aluno = buscarEntidade(id);
		boolean perfilMudou = !aluno.getNivelExperiencia().equals(request.nivelExperiencia())
				|| !aluno.getFrequenciaSemanal().equals(request.frequenciaSemanal());
		if (!aluno.getCpf().equals(request.cpf()) && repository.existsByCpf(request.cpf())) {
			throw new RegraDeNegocioException("Ja existe aluno com este CPF.");
		}
		if (!aluno.getEmail().equals(request.email()) && repository.existsByEmail(request.email())) {
			throw new RegraDeNegocioException("Ja existe aluno com este e-mail.");
		}
		aplicar(aluno, request);
		aluno = repository.save(aluno);
		if (perfilMudou) {
			treinoAtivoService.sincronizar(aluno);
		}
		return Mapper.toResponse(aluno);
	}

	@Transactional
	public void excluir(Long id) {
		repository.delete(buscarEntidade(id));
	}

	public Aluno buscarEntidade(Long id) {
		return repository.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Aluno não encontrado com ID: " + id));
	}

	public void ativar(Aluno aluno) {
		aluno.setStatus(StatusAluno.ATIVO);
		repository.save(aluno);
	}

	public void inativar(Aluno aluno) {
		aluno.setStatus(StatusAluno.INATIVO);
		repository.save(aluno);
	}

	private void aplicar(Aluno aluno, AlunoRequest request) {
		aluno.setNome(request.nome());
		aluno.setCpf(request.cpf());
		aluno.setTelefone(request.telefone());
		aluno.setEmail(request.email());
		aluno.setDataNascimento(request.dataNascimento());
		aluno.setPeso(request.peso());
		aluno.setAltura(request.altura());
		aluno.setNivelExperiencia(request.nivelExperiencia());
		aluno.setFrequenciaSemanal(request.frequenciaSemanal());
		aluno.setStatus(request.status() == null ? StatusAluno.ATIVO : request.status());
	}
}


