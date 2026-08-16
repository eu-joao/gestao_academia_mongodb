package br.edu.ifpb.academia.entity;

import br.edu.ifpb.academia.enums.NivelExperiencia;
import br.edu.ifpb.academia.enums.TipoEstrutural;
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
public class ProgramaTreino {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private NivelExperiencia nivelExperiencia;

	@Column(nullable = false)
	private Integer frequenciaSemanal;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private TipoEstrutural tipoEstrutural;

	@Column(nullable = false)
	private String descricao;
}