package br.edu.ifpb.academia.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifpb.academia.entity.Cobranca;
import br.edu.ifpb.academia.enums.StatusCobranca;

public interface CobrancaRepository extends JpaRepository<Cobranca, Long> {
	List<Cobranca> findByStatusAndDataVencimentoBefore(StatusCobranca status, LocalDate dataLimite);
	List<Cobranca> findByStatusAndDataVencimentoBetween(StatusCobranca status, LocalDate inicio, LocalDate fim);
	boolean existsByContratoAlunoIdAndStatus(Long alunoId, StatusCobranca status);
}
