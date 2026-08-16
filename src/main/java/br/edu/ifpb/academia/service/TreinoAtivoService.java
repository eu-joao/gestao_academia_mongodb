package br.edu.ifpb.academia.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.edu.ifpb.academia.dto.Mapper;
import br.edu.ifpb.academia.dto.response.Responses.TreinoAtivoResponse;
import br.edu.ifpb.academia.entity.Aluno;
import br.edu.ifpb.academia.entity.ProgramaTreino;
import br.edu.ifpb.academia.entity.TreinoAtivo;
import br.edu.ifpb.academia.enums.StatusTreinoAtivo;
import br.edu.ifpb.academia.exception.RecursoNaoEncontradoException;
import br.edu.ifpb.academia.repository.TreinoAtivoRepository;
import lombok.RequiredArgsConstructor;

@Service
@SuppressWarnings("null")
@RequiredArgsConstructor
public class TreinoAtivoService {

	private final TreinoAtivoRepository repository;
	private final ProgramaTreinoService programaTreinoService;


	@Transactional(readOnly = true)
	public List<TreinoAtivoResponse> listar() {
		return repository.findAll().stream().map(Mapper::toResponse).toList();
	}

	@Transactional(readOnly = true)
	public TreinoAtivoResponse buscar(Long id) {
		return Mapper.toResponse(repository.findById(id)
				.orElseThrow(() -> new RecursoNaoEncontradoException("Treino ativo nao encontrado.")));
	}

	@Transactional(readOnly = true)
	public List<TreinoAtivoResponse> listarPorAluno(Long alunoId) {
		return repository.findByAlunoIdOrderByDataInicioDesc(alunoId).stream().map(Mapper::toResponse).toList();
	}

	@Transactional
	public void sincronizar(Aluno aluno) {
		ProgramaTreino programa = programaTreinoService.buscarPorPerfil(aluno.getNivelExperiencia(),
				aluno.getFrequenciaSemanal());
		repository.findByAlunoIdAndStatus(aluno.getId(), StatusTreinoAtivo.ATIVO).ifPresent(atual -> {
			if (!atual.getProgramaTreino().getId().equals(programa.getId())) {
				atual.setStatus(StatusTreinoAtivo.ENCERRADO);
				atual.setDataFim(LocalDate.now());
				repository.save(atual);
			}
		});
		if (repository.findByAlunoIdAndStatus(aluno.getId(), StatusTreinoAtivo.ATIVO).isEmpty()) {
			TreinoAtivo treino = new TreinoAtivo();
			treino.setAluno(aluno);
			treino.setProgramaTreino(programa);
			treino.setDataInicio(LocalDate.now());
			treino.setStatus(StatusTreinoAtivo.ATIVO);
			repository.save(treino);
		}
	}
}

