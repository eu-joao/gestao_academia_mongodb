package br.edu.ifpb.academia.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
public class DiaTreinoExercicio {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	private DiaTreino diaTreino;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	private Exercicio exercicio;

	@Column(nullable = false)
	private Integer series;

	@Column(nullable = false)
	private Integer repeticoes;

	@Column(nullable = false)
	private Integer ordem;
}