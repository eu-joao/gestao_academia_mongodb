package br.edu.ifpb.academia.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import br.edu.ifpb.academia.enums.FormaPagamento;
import br.edu.ifpb.academia.enums.StatusCobranca;
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
public class Cobranca {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	private Contrato contrato;

	@Column(nullable = false)
	private LocalDate dataVencimento;

	@Column(nullable = false, precision = 10, scale = 2)
	private BigDecimal valor;

	private LocalDate dataPagamento;

	@Enumerated(EnumType.STRING)
	private FormaPagamento formaPagamento;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private StatusCobranca status = StatusCobranca.PENDENTE;

	public boolean isPaga() {
		return status == StatusCobranca.PAGO;
	}
}