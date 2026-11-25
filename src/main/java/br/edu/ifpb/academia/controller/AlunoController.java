package br.edu.ifpb.academia.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import br.edu.ifpb.academia.dto.request.AlunoRequest;
import br.edu.ifpb.academia.dto.response.Responses.AlunoResponse;
import br.edu.ifpb.academia.service.AlunoService;
import br.edu.ifpb.academia.service.StorageService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/alunos")
@RequiredArgsConstructor
public class AlunoController {
	
	private final AlunoService service;
	private final StorageService storageService; // <-- Adicionado aqui

	@Operation(summary = "Lista alunos")
	@GetMapping
	public List<AlunoResponse> listar() {
		return service.listar();
	}

	@Operation(summary = "Busca aluno por ID")
	@GetMapping("/{id}")
	public AlunoResponse buscar(@PathVariable Long id) {
		return service.buscar(id);
	}

	@Operation(summary = "Cria aluno e treino ativo automaticamente")
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping
	public AlunoResponse criar(@Valid @RequestBody AlunoRequest request) {
		return service.criar(request);
	}

	@Operation(summary = "Atualiza aluno e recria treino ativo quando o perfil muda")
	@PutMapping("/{id}")
	public AlunoResponse atualizar(@PathVariable Long id, @Valid @RequestBody AlunoRequest request) {
		return service.atualizar(id, request);
	}

	@Operation(summary = "Remove aluno")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{id}")
	public void excluir(@PathVariable Long id) {
		service.excluir(id);
	}

	// ==========================================
	// ENDPOINT NOVO DO S3 (Upload de Foto)
	// ==========================================
	@Operation(summary = "Faz upload da foto de perfil do aluno")
	@PostMapping(value = "/{id}/foto", consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE) // <-- Adicionado o consumes
	public ResponseEntity<String> uploadFoto(
			@PathVariable Long id, 
			@RequestParam("file") MultipartFile file) {
		
		String caminhoArquivo = storageService.uploadFile(file, "fotos-alunos");
		service.atualizarFoto(id, caminhoArquivo);
		return ResponseEntity.ok("Foto enviada com sucesso! Caminho: " + caminhoArquivo);
	}
}