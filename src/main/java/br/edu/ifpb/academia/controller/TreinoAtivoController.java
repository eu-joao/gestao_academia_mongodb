package br.edu.ifpb.academia.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifpb.academia.dto.response.Responses.TreinoAtivoResponse;
import br.edu.ifpb.academia.service.TreinoAtivoService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/treinos-ativos")
@RequiredArgsConstructor
public class TreinoAtivoController {
	private final TreinoAtivoService service;


	@Operation(summary = "Lista treinos ativos e historico")
	@GetMapping
	public List<TreinoAtivoResponse> listar() {
		return service.listar();
	}

	@Operation(summary = "Busca treino ativo por ID")
	@GetMapping("/{id}")
	public TreinoAtivoResponse buscar(@PathVariable Long id) {
		return service.buscar(id);
	}

	@Operation(summary = "Lista historico de treinos de um aluno")
	@GetMapping("/aluno/{alunoId}")
	public List<TreinoAtivoResponse> listarPorAluno(@PathVariable Long alunoId) {
		return service.listarPorAluno(alunoId);
	}
}
