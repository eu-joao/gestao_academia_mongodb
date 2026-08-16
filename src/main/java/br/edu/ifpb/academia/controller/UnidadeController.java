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

import br.edu.ifpb.academia.dto.request.UnidadeRequest;
import br.edu.ifpb.academia.dto.response.Responses.UnidadeResponse;
import br.edu.ifpb.academia.service.UnidadeService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/unidades")
@RequiredArgsConstructor
public class UnidadeController {
	private final UnidadeService service;


	@Operation(summary = "Lista unidades")
	@GetMapping
	public List<UnidadeResponse> listar() {
		return service.listar();
	}

	@Operation(summary = "Busca unidade por ID")
	@GetMapping("/{id}")
	public UnidadeResponse buscar(@PathVariable Long id) {
		return service.buscar(id);
	}

	@Operation(summary = "Cria unidade")
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping
	public UnidadeResponse criar(@Valid @RequestBody UnidadeRequest request) {
		return service.criar(request);
	}

	@Operation(summary = "Atualiza unidade")
	@PutMapping("/{id}")
	public UnidadeResponse atualizar(@PathVariable Long id, @Valid @RequestBody UnidadeRequest request) {
		return service.atualizar(id, request);
	}

	@Operation(summary = "Remove unidade")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{id}")
	public void excluir(@PathVariable Long id) {
		service.excluir(id);
	}
}
