package br.edu.ifpb.academia.dto.request;

import java.time.LocalDate;

import br.edu.ifpb.academia.enums.FormaPagamento;
import br.edu.ifpb.academia.enums.StatusCobranca;
import jakarta.validation.constraints.NotNull;

public record CobrancaRequest(@NotNull Long contratoId, @NotNull LocalDate dataVencimento,
		LocalDate dataPagamento, FormaPagamento formaPagamento, StatusCobranca status) {
}
