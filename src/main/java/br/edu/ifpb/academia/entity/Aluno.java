package br.edu.ifpb.academia.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import br.edu.ifpb.academia.enums.NivelExperiencia;
import br.edu.ifpb.academia.enums.StatusAluno;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Aluno {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String nome;

	@Column(nullable = false, unique = true)
	private String cpf;

	@Column(nullable = false)
	private String telefone;

	@Column(nullable = false, unique = true)
	private String email;

	@Column(nullable = false)
	private LocalDate dataNascimento;

	@Column(nullable = false, precision = 6, scale = 2)
	private BigDecimal peso;

	@Column(nullable = false, precision = 4, scale = 2)
	private BigDecimal altura;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private NivelExperiencia nivelExperiencia;

	@Column(nullable = false)
	private Integer frequenciaSemanal;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private StatusAluno status = StatusAluno.ATIVO;

	public boolean isAtivo() {
		return status == StatusAluno.ATIVO;
	}
}