package br.edu.ifpb.academia.dto.request;

import java.time.LocalDate;

import br.edu.ifpb.academia.enums.StatusContrato;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

public record ContratoRequest(@NotNull Long alunoId, @NotNull Long unidadeId, @NotNull Long planoId,
		@NotNull @PastOrPresent LocalDate dataInicio, StatusContrato status) {
}
