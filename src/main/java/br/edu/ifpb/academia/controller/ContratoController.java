package br.edu.ifpb.academia.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifpb.academia.dto.request.ContratoRequest;
import br.edu.ifpb.academia.dto.response.Responses.ContratoResponse;
import br.edu.ifpb.academia.service.ContratoService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/contratos")
@RequiredArgsConstructor
public class ContratoController {
	private final ContratoService service;


	@Operation(summary = "Lista contratos")
	@GetMapping
	public List<ContratoResponse> listar() {
		return service.listar();
	}

	@Operation(summary = "Busca contrato por ID")
	@GetMapping("/{id}")
	public ContratoResponse buscar(@PathVariable Long id) {
		return service.buscar(id);
	}

	@Operation(summary = "Cria contrato e gera cobranca automaticamente")
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping
	public ContratoResponse criar(@Valid @RequestBody ContratoRequest request) {
		return service.criar(request);
	}

	@Operation(summary = "Atualiza contrato")
	@PutMapping("/{id}")
	public ContratoResponse atualizar(@PathVariable Long id, @Valid @RequestBody ContratoRequest request) {
		return service.atualizar(id, request);
	}

	@Operation(summary = "Cancela contrato e inativa aluno")
	@PutMapping("/{id}/cancelar")
	public ContratoResponse cancelar(@PathVariable Long id) {
		return service.cancelar(id);
	}

	@Operation(summary = "Remove contrato")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{id}")
	public void excluir(@PathVariable Long id) {
		service.excluir(id);
	}
}
