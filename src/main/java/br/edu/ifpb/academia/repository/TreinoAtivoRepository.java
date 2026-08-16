package br.edu.ifpb.academia.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifpb.academia.entity.TreinoAtivo;
import br.edu.ifpb.academia.enums.StatusTreinoAtivo;

public interface TreinoAtivoRepository extends JpaRepository<TreinoAtivo, Long> {
	Optional<TreinoAtivo> findByAlunoIdAndStatus(Long alunoId, StatusTreinoAtivo status);
	List<TreinoAtivo> findByAlunoIdOrderByDataInicioDesc(Long alunoId);
}
