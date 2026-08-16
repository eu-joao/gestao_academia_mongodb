package br.edu.ifpb.academia.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record DiaTreinoRequest(@NotNull Long programaTreinoId, @NotBlank String nomeDia,
		@NotNull @Positive Integer ordem) {
}
