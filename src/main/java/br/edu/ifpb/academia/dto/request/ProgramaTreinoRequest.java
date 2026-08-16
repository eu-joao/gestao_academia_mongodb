package br.edu.ifpb.academia.dto.request;

import br.edu.ifpb.academia.enums.NivelExperiencia;
import br.edu.ifpb.academia.enums.TipoEstrutural;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProgramaTreinoRequest(@NotNull NivelExperiencia nivelExperiencia,
		@NotNull @Min(3) @Max(6) Integer frequenciaSemanal, @NotNull TipoEstrutural tipoEstrutural,
		@NotBlank String descricao) {
}
