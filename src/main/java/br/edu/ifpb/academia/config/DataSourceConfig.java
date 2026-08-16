package br.edu.ifpb.academia.config;

import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.zaxxer.hikari.HikariDataSource;

@Configuration(proxyBeanMethods = false)
public class DataSourceConfig {

	@Bean
	DataSource dataSource(Environment environment) {
		String connectionString = environment.getRequiredProperty("SUPABASE_DATABASE_URL");
		URI connectionUri = parseConnectionUri(connectionString);
		String[] credentials = parseCredentials(connectionUri);

		HikariDataSource dataSource = new HikariDataSource();
		dataSource.setJdbcUrl(toJdbcUrl(connectionUri));
		dataSource.setUsername(decode(credentials[0]));
		dataSource.setPassword(decode(credentials[1]));
		return dataSource;
	}

	private URI parseConnectionUri(String connectionString) {
		URI uri;
		try {
			uri = URI.create(connectionString);
		} catch (IllegalArgumentException exception) {
			throw new IllegalStateException("SUPABASE_DATABASE_URL não é uma URI válida", exception);
		}

		if (!"postgresql".equalsIgnoreCase(uri.getScheme())
				&& !"postgres".equalsIgnoreCase(uri.getScheme())) {
			throw new IllegalStateException("SUPABASE_DATABASE_URL deve usar o esquema postgresql://");
		}
		if (uri.getHost() == null || uri.getPath() == null || uri.getPath().equals("/")
				|| uri.getRawUserInfo() == null) {
			throw new IllegalStateException(
					"SUPABASE_DATABASE_URL deve conter host, banco, usuário e senha");
		}
		return uri;
	}

	private String[] parseCredentials(URI uri) {
		String[] credentials = uri.getRawUserInfo().split(":", 2);
		if (credentials.length != 2 || credentials[0].isBlank() || credentials[1].isBlank()) {
			throw new IllegalStateException(
					"SUPABASE_DATABASE_URL deve conter usuário e senha no userinfo");
		}
		return credentials;
	}

	private String toJdbcUrl(URI uri) {
		String query = uri.getRawQuery();
		String sslQuery = query == null || query.isBlank()
				? "sslmode=require"
				: query.contains("sslmode=") ? query : query + "&sslmode=require";
		int port = uri.getPort() > 0 ? uri.getPort() : 5432;
		return "jdbc:postgresql://" + uri.getHost() + ":" + port + uri.getRawPath() + "?" + sslQuery;
	}

	private String decode(String value) {
		return URLDecoder.decode(value.replace("+", "%2B"), StandardCharsets.UTF_8);
	}
}