package joyfe.joyfeSpring.apiController;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class ApiDescription {
    @Value("${joyfe.openapi.dev-url}")
    private String devUrl;

    @Value("${joyfe.openapi.prod-url}")
    private String prodUrl;

	@Bean
	public OpenAPI myOpenAPI() {
		Server devServer = new Server();
		devServer.setUrl(devUrl);
		return new OpenAPI().servers(List.of(devServer)).info(new Info().description("API REST JOYFE DEV").termsOfService("Términos del servicio").contact(new Contact().name("Colegio Joyfe").email("correo@correo.es")));
	}
}
