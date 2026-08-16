package br.edu.ifpb.academia.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifpb.academia.entity.Aluno;

public interface AlunoRepository extends JpaRepository<Aluno, Long> {
	boolean existsByCpf(String cpf);
	boolean existsByEmail(String email);
}
