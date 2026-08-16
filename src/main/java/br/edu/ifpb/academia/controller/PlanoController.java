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

import br.edu.ifpb.academia.dto.request.PlanoRequest;
import br.edu.ifpb.academia.dto.response.Responses.PlanoResponse;
import br.edu.ifpb.academia.service.PlanoService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/planos")
@RequiredArgsConstructor
public class PlanoController {
	private final PlanoService service;


	@Operation(summary = "Lista planos")
	@GetMapping
	public List<PlanoResponse> listar() {
		return service.listar();
	}

	@Operation(summary = "Busca plano por ID")
	@GetMapping("/{id}")
	public PlanoResponse buscar(@PathVariable Long id) {
		return service.buscar(id);
	}

	@Operation(summary = "Cria plano")
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping
	public PlanoResponse criar(@Valid @RequestBody PlanoRequest request) {
		return service.criar(request);
	}

	@Operation(summary = "Atualiza plano")
	@PutMapping("/{id}")
	public PlanoResponse atualizar(@PathVariable Long id, @Valid @RequestBody PlanoRequest request) {
		return service.atualizar(id, request);
	}

	@Operation(summary = "Remove plano")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{id}")
	public void excluir(@PathVariable Long id) {
		service.excluir(id);
	}
}
