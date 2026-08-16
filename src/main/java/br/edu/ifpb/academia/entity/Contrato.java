package br.edu.ifpb.academia.entity;

import java.time.LocalDate;

import br.edu.ifpb.academia.enums.StatusContrato;
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
public class Contrato {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	private Aluno aluno;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	private Unidade unidade;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	private Plano plano;

	@Column(nullable = false)
	private LocalDate dataInicio;

	@Column(nullable = false)
	private LocalDate dataTermino;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private StatusContrato status = StatusContrato.ATIVO;

	public void setPlano(Plano plano) {
		this.plano = plano;
		recalcularDataTermino();
	}

	public void setDataInicio(LocalDate dataInicio) {
		this.dataInicio = dataInicio;
		recalcularDataTermino();
	}

	public boolean isAtivo() {
		return status == StatusContrato.ATIVO;
	}

	private void recalcularDataTermino() {
		if (dataInicio != null && plano != null && plano.getDuracao() != null) {
			dataTermino = dataInicio.plusDays(plano.getDuracao());
		}
	}
}