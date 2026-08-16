package br.edu.ifpb.academia.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifpb.academia.entity.Unidade;

public interface UnidadeRepository extends JpaRepository<Unidade, Long> {
	boolean existsByCnpj(String cnpj);
}
