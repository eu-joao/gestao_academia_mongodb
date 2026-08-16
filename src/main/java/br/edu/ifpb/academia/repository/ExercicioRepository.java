package br.edu.ifpb.academia.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifpb.academia.entity.Exercicio;

public interface ExercicioRepository extends JpaRepository<Exercicio, Long> {
	boolean existsByNome(String nome);
}
