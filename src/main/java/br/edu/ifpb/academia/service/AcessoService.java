package br.edu.ifpb.academia.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.edu.ifpb.academia.dto.Mapper;
import br.edu.ifpb.academia.dto.response.Responses.AcessoResponse;
import br.edu.ifpb.academia.entity.Aluno;
import br.edu.ifpb.academia.enums.StatusCobranca;
import br.edu.ifpb.academia.enums.StatusContrato;
import br.edu.ifpb.academia.exception.RegraDeNegocioException;
import br.edu.ifpb.academia.repository.CobrancaRepository;
import br.edu.ifpb.academia.repository.ContratoRepository;
import lombok.RequiredArgsConstructor;

@Service
@SuppressWarnings("null")
@RequiredArgsConstructor
public class AcessoService {

	private final AlunoService alunoService;
	private final ContratoRepository contratoRepository;
	private final CobrancaRepository cobrancaRepository;
	private final CobrancaService cobrancaService;


	@Transactional
	public AcessoResponse verificar(Long alunoId) {
		cobrancaService.atualizarAtrasadas();
		Aluno aluno = alunoService.buscarEntidade(alunoId);
		if (!aluno.isAtivo()) {
			throw new RegraDeNegocioException("Aluno inativo. Acesso negado.");
		}
		if (contratoRepository.findFirstByAlunoIdAndStatusOrderByDataInicioDesc(alunoId, StatusContrato.ATIVO)
				.isEmpty()) {
			return Mapper.acesso(alunoId, false, "Aluno sem contrato ativo.");
		}
		if (cobrancaRepository.existsByContratoAlunoIdAndStatus(alunoId, StatusCobranca.ATRASADO)) {
			return Mapper.acesso(alunoId, false, "Aluno possui cobranca atrasada.");
		}
		return Mapper.acesso(alunoId, true, "Acesso liberado.");
	}
}
