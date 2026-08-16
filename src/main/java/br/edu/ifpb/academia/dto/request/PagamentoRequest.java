package br.edu.ifpb.academia.dto.request;

import java.time.LocalDate;

import br.edu.ifpb.academia.enums.FormaPagamento;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

public record PagamentoRequest(@NotNull @PastOrPresent LocalDate dataPagamento,
		@NotNull FormaPagamento formaPagamento) {
}
