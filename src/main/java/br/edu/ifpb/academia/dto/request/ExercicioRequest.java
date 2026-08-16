package br.edu.ifpb.academia.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ExercicioRequest(@NotBlank String nome, @NotBlank String musculaturaAlvo) {
}
