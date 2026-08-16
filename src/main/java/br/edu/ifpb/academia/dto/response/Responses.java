package br.edu.ifpb.academia.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

import br.edu.ifpb.academia.enums.FormaPagamento;
import br.edu.ifpb.academia.enums.NivelExperiencia;
import br.edu.ifpb.academia.enums.StatusAluno;
import br.edu.ifpb.academia.enums.StatusCobranca;
import br.edu.ifpb.academia.enums.StatusContrato;
import br.edu.ifpb.academia.enums.StatusTreinoAtivo;
import br.edu.ifpb.academia.enums.TipoEstrutural;
import br.edu.ifpb.academia.enums.TipoPlano;

public final class Responses {
	private Responses() {
	}

	public record UnidadeResponse(Long id, String nome, String endereco, String cnpj, String telefone) {
	}

	public record AlunoResponse(Long id, String nome, String cpf, String telefone, String email,
			LocalDate dataNascimento, BigDecimal peso, BigDecimal altura, NivelExperiencia nivelExperiencia,
			Integer frequenciaSemanal, StatusAluno status) {
	}

	public record PlanoResponse(Long id, TipoPlano tipo, BigDecimal valor, Integer duracao,
			BigDecimal percentualDesconto, String descricao) {
	}

	public record ProgramaTreinoResponse(Long id, NivelExperiencia nivelExperiencia, Integer frequenciaSemanal,
			TipoEstrutural tipoEstrutural, String descricao) {
	}

	public record DiaTreinoResponse(Long id, Long programaTreinoId, String nomeDia, Integer ordem) {
	}

	public record ExercicioResponse(Long id, String nome, String musculaturaAlvo) {
	}

	public record DiaTreinoExercicioResponse(Long id, Long diaTreinoId, Long exercicioId, String exercicioNome,
			Integer series, Integer repeticoes, Integer ordem) {
	}

	public record TreinoAtivoResponse(Long id, Long alunoId, String alunoNome, Long programaTreinoId,
			String programaDescricao, LocalDate dataInicio, LocalDate dataFim, StatusTreinoAtivo status) {
	}

	public record ContratoResponse(Long id, Long alunoId, String alunoNome, Long unidadeId, String unidadeNome,
			Long planoId, TipoPlano planoTipo, LocalDate dataInicio, LocalDate dataTermino, StatusContrato status) {
	}

	public record CobrancaResponse(Long id, Long contratoId, LocalDate dataVencimento, BigDecimal valor,
			LocalDate dataPagamento, FormaPagamento formaPagamento, StatusCobranca status) {
	}

	public record AcessoResponse(Long alunoId, boolean liberado, String motivo) {
	}
}
