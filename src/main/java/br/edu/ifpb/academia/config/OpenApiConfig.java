package br.edu.ifpb.academia.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

	@Bean
	public OpenAPI api() {
		return new OpenAPI().info(new Info()
				.title("Sistema de Gestao de Academia")
				.version("1.0.0")
				.description("API REST para contratos, cobrancas, treinos e acesso de academia."));
	}
}
