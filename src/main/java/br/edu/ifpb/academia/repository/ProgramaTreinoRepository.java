package br.edu.ifpb.academia.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifpb.academia.entity.ProgramaTreino;
import br.edu.ifpb.academia.enums.NivelExperiencia;

public interface ProgramaTreinoRepository extends JpaRepository<ProgramaTreino, Long> {
	Optional<ProgramaTreino> findByNivelExperienciaAndFrequenciaSemanal(NivelExperiencia nivelExperiencia,
			Integer frequenciaSemanal);

	boolean existsByNivelExperienciaAndFrequenciaSemanal(NivelExperiencia nivelExperiencia, Integer frequenciaSemanal);
}
