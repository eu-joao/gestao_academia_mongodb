package br.edu.ifpb.academia.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;

import br.edu.ifpb.academia.enums.NivelExperiencia;
import br.edu.ifpb.academia.enums.StatusAluno;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Positive;

public record AlunoRequest(@NotBlank String nome, @NotBlank String cpf, @NotBlank String telefone,
		@NotBlank @Email String email, @NotNull @Past LocalDate dataNascimento, @NotNull @Positive BigDecimal peso,
		@NotNull @Positive BigDecimal altura, @NotNull NivelExperiencia nivelExperiencia,
		@NotNull @Min(3) @Max(6) Integer frequenciaSemanal, StatusAluno status) {
}
