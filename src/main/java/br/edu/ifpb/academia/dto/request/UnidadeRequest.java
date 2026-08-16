package br.edu.ifpb.academia.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UnidadeRequest(@NotBlank String nome, @NotBlank String endereco, @NotBlank String cnpj,
		@NotBlank String telefone) {
}
