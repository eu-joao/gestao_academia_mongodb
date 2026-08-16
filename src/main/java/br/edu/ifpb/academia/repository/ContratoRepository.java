package br.edu.ifpb.academia.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifpb.academia.entity.Contrato;
import br.edu.ifpb.academia.enums.StatusContrato;

public interface ContratoRepository extends JpaRepository<Contrato, Long> {
	boolean existsByAlunoIdAndStatus(Long alunoId, StatusContrato status);
	Optional<Contrato> findFirstByAlunoIdAndStatusOrderByDataInicioDesc(Long alunoId, StatusContrato status);
}
