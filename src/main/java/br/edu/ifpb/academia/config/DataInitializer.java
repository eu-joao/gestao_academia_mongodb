package br.edu.ifpb.academia.config;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.edu.ifpb.academia.entity.Plano;
import br.edu.ifpb.academia.entity.ProgramaTreino;
import br.edu.ifpb.academia.enums.NivelExperiencia;
import br.edu.ifpb.academia.enums.TipoEstrutural;
import br.edu.ifpb.academia.enums.TipoPlano;
import br.edu.ifpb.academia.repository.PlanoRepository;
import br.edu.ifpb.academia.repository.ProgramaTreinoRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

	private final PlanoRepository planoRepository;
	private final ProgramaTreinoRepository programaTreinoRepository;


	@Override
	@Transactional
	public void run(String... args) {
		criarPlanos();
		criarProgramas();
	}

	private void criarPlanos() {
		List<Plano> planos = List.of(
				plano(TipoPlano.DIARIO, "20.00", 1, "0.00", "Acesso diario a qualquer unidade"),
				plano(TipoPlano.MENSAL, "120.00", 30, "0.00", "Acesso mensal a todas as unidades"),
				plano(TipoPlano.TRIMESTRAL, "330.00", 90, "8.33", "Plano trimestral com desconto"),
				plano(TipoPlano.SEMESTRAL, "600.00", 180, "16.67", "Plano semestral com desconto"),
				plano(TipoPlano.ANUAL, "1080.00", 365, "25.00", "Plano anual com maior desconto"));
		for (Plano plano : planos) {
			if (!planoRepository.existsByTipo(plano.getTipo())) {
				planoRepository.save(plano);
			}
		}
	}

	private void criarProgramas() {
		for (NivelExperiencia nivel : NivelExperiencia.values()) {
			criarPrograma(nivel, 3, TipoEstrutural.FULLBODY);
			criarPrograma(nivel, 4, TipoEstrutural.UPPER_LOWER);
			criarPrograma(nivel, 5, TipoEstrutural.PPL_UPPER_LOWER);
			criarPrograma(nivel, 6, TipoEstrutural.PPL_2X);
		}
	}

	private void criarPrograma(NivelExperiencia nivel, Integer frequencia, TipoEstrutural tipo) {
		if (!programaTreinoRepository.existsByNivelExperienciaAndFrequenciaSemanal(nivel, frequencia)) {
			ProgramaTreino programa = new ProgramaTreino();
			programa.setNivelExperiencia(nivel);
			programa.setFrequenciaSemanal(frequencia);
			programa.setTipoEstrutural(tipo);
			programa.setDescricao("Programa " + tipo.name() + " para " + nivel.name()
					+ " com frequencia de " + frequencia + " dias por semana");
			programaTreinoRepository.save(programa);
		}
	}

	private Plano plano(TipoPlano tipo, String valor, Integer duracao, String desconto, String descricao) {
		Plano plano = new Plano();
		plano.setTipo(tipo);
		plano.setValor(new BigDecimal(valor));
		plano.setDuracao(duracao);
		plano.setPercentualDesconto(new BigDecimal(desconto));
		plano.setDescricao(descricao);
		return plano;
	}
}
