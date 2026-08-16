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

import br.edu.ifpb.academia.dto.request.CobrancaRequest;
import br.edu.ifpb.academia.dto.request.PagamentoRequest;
import br.edu.ifpb.academia.dto.response.Responses.CobrancaResponse;
import br.edu.ifpb.academia.service.CobrancaService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/cobrancas")
@RequiredArgsConstructor
public class CobrancaController {
	private final CobrancaService service;


	@Operation(summary = "Lista cobrancas")
	@GetMapping
	public List<CobrancaResponse> listar() {
		return service.listar();
	}

	@Operation(summary = "Busca cobranca por ID")
	@GetMapping("/{id}")
	public CobrancaResponse buscar(@PathVariable Long id) {
		return service.buscar(id);
	}

	@Operation(summary = "Cria cobranca com valor derivado do plano")
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping
	public CobrancaResponse criar(@Valid @RequestBody CobrancaRequest request) {
		return service.criar(request);
	}

	@Operation(summary = "Atualiza cobranca mantendo valor derivado do plano")
	@PutMapping("/{id}")
	public CobrancaResponse atualizar(@PathVariable Long id, @Valid @RequestBody CobrancaRequest request) {
		return service.atualizar(id, request);
	}

	@Operation(summary = "Registra pagamento")
	@PutMapping("/{id}/pagar")
	public CobrancaResponse pagar(@PathVariable Long id, @Valid @RequestBody PagamentoRequest request) {
		return service.pagar(id, request);
	}

	@Operation(summary = "Remove cobranca")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{id}")
	public void excluir(@PathVariable Long id) {
		service.excluir(id);
	}
}
