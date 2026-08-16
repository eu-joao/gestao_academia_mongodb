package br.edu.ifpb.academia.dto;

import br.edu.ifpb.academia.dto.response.Responses.AcessoResponse;
import br.edu.ifpb.academia.dto.response.Responses.AlunoResponse;
import br.edu.ifpb.academia.dto.response.Responses.CobrancaResponse;
import br.edu.ifpb.academia.dto.response.Responses.ContratoResponse;
import br.edu.ifpb.academia.dto.response.Responses.DiaTreinoExercicioResponse;
import br.edu.ifpb.academia.dto.response.Responses.DiaTreinoResponse;
import br.edu.ifpb.academia.dto.response.Responses.ExercicioResponse;
import br.edu.ifpb.academia.dto.response.Responses.PlanoResponse;
import br.edu.ifpb.academia.dto.response.Responses.ProgramaTreinoResponse;
import br.edu.ifpb.academia.dto.response.Responses.TreinoAtivoResponse;
import br.edu.ifpb.academia.dto.response.Responses.UnidadeResponse;
import br.edu.ifpb.academia.entity.Aluno;
import br.edu.ifpb.academia.entity.Cobranca;
import br.edu.ifpb.academia.entity.Contrato;
import br.edu.ifpb.academia.entity.DiaTreino;
import br.edu.ifpb.academia.entity.DiaTreinoExercicio;
import br.edu.ifpb.academia.entity.Exercicio;
import br.edu.ifpb.academia.entity.Plano;
import br.edu.ifpb.academia.entity.ProgramaTreino;
import br.edu.ifpb.academia.entity.TreinoAtivo;
import br.edu.ifpb.academia.entity.Unidade;

public final class Mapper {
	private Mapper() {
	}

	public static UnidadeResponse toResponse(Unidade unidade) {
		return new UnidadeResponse(unidade.getId(), unidade.getNome(), unidade.getEndereco(), unidade.getCnpj(),
				unidade.getTelefone());
	}

	public static AlunoResponse toResponse(Aluno aluno) {
		return new AlunoResponse(aluno.getId(), aluno.getNome(), aluno.getCpf(), aluno.getTelefone(),
				aluno.getEmail(), aluno.getDataNascimento(), aluno.getPeso(), aluno.getAltura(),
				aluno.getNivelExperiencia(), aluno.getFrequenciaSemanal(), aluno.getStatus());
	}

	public static PlanoResponse toResponse(Plano plano) {
		return new PlanoResponse(plano.getId(), plano.getTipo(), plano.getValor(), plano.getDuracao(),
				plano.getPercentualDesconto(), plano.getDescricao());
	}

	public static ProgramaTreinoResponse toResponse(ProgramaTreino programa) {
		return new ProgramaTreinoResponse(programa.getId(), programa.getNivelExperiencia(),
				programa.getFrequenciaSemanal(), programa.getTipoEstrutural(), programa.getDescricao());
	}

	public static DiaTreinoResponse toResponse(DiaTreino dia) {
		return new DiaTreinoResponse(dia.getId(), dia.getProgramaTreino().getId(), dia.getNomeDia(),
				dia.getOrdem());
	}

	public static ExercicioResponse toResponse(Exercicio exercicio) {
		return new ExercicioResponse(exercicio.getId(), exercicio.getNome(), exercicio.getMusculaturaAlvo());
	}

	public static DiaTreinoExercicioResponse toResponse(DiaTreinoExercicio item) {
		return new DiaTreinoExercicioResponse(item.getId(), item.getDiaTreino().getId(),
				item.getExercicio().getId(), item.getExercicio().getNome(), item.getSeries(),
				item.getRepeticoes(), item.getOrdem());
	}

	public static TreinoAtivoResponse toResponse(TreinoAtivo treino) {
		return new TreinoAtivoResponse(treino.getId(), treino.getAluno().getId(), treino.getAluno().getNome(),
				treino.getProgramaTreino().getId(), treino.getProgramaTreino().getDescricao(),
				treino.getDataInicio(), treino.getDataFim(), treino.getStatus());
	}

	public static ContratoResponse toResponse(Contrato contrato) {
		return new ContratoResponse(contrato.getId(), contrato.getAluno().getId(), contrato.getAluno().getNome(),
				contrato.getUnidade().getId(), contrato.getUnidade().getNome(), contrato.getPlano().getId(),
				contrato.getPlano().getTipo(), contrato.getDataInicio(), contrato.getDataTermino(),
				contrato.getStatus());
	}

	public static CobrancaResponse toResponse(Cobranca cobranca) {
		return new CobrancaResponse(cobranca.getId(), cobranca.getContrato().getId(),
				cobranca.getDataVencimento(), cobranca.getValor(), cobranca.getDataPagamento(),
				cobranca.getFormaPagamento(), cobranca.getStatus());
	}

	public static AcessoResponse acesso(Long alunoId, boolean liberado, String motivo) {
		return new AcessoResponse(alunoId, liberado, motivo);
	}
}
