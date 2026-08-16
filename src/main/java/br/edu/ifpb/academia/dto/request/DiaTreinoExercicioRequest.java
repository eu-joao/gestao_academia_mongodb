package br.edu.ifpb.academia.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record DiaTreinoExercicioRequest(@NotNull Long diaTreinoId, @NotNull Long exercicioId,
		@NotNull @Positive Integer series, @NotNull @Positive Integer repeticoes,
		@NotNull @Positive Integer ordem) {
}
