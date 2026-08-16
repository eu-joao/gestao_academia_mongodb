package br.edu.ifpb.academia.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifpb.academia.entity.Plano;
import br.edu.ifpb.academia.enums.TipoPlano;

public interface PlanoRepository extends JpaRepository<Plano, Long> {
	boolean existsByTipo(TipoPlano tipo);
}
