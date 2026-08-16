package br.edu.ifpb.academia.dto.request;

import java.math.BigDecimal;

import br.edu.ifpb.academia.enums.TipoPlano;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record PlanoRequest(@NotNull TipoPlano tipo, @NotNull @Positive BigDecimal valor,
		@NotNull @Positive Integer duracao, @NotNull @PositiveOrZero BigDecimal percentualDesconto,
		@NotBlank String descricao) {
}
