package br.edu.ifpb.academia.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifpb.academia.dto.response.Responses.AcessoResponse;
import br.edu.ifpb.academia.service.AcessoService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/acessos")
@RequiredArgsConstructor
public class AcessoController {
	private final AcessoService service;


	@Operation(summary = "Verifica acesso do aluno na catraca")
	@GetMapping("/alunos/{alunoId}")
	public AcessoResponse verificar(@PathVariable Long alunoId) {
		return service.verificar(alunoId);
	}
}
