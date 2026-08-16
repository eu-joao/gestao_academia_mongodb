package br.edu.ifpb.academia.entity;

import java.time.LocalDate;

import br.edu.ifpb.academia.enums.StatusTreinoAtivo;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class TreinoAtivo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	private Aluno aluno;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	private ProgramaTreino programaTreino;

	@Column(nullable = false)
	private LocalDate dataInicio;

	private LocalDate dataFim;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private StatusTreinoAtivo status = StatusTreinoAtivo.ATIVO;
}