package joyfe.joyfeSpring.apiController;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
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
		devServer.setDescription("Desarrollo");
		
		Server prodServer = new Server();
		prodServer.setUrl(prodUrl);
		prodServer.setDescription("Produccion");
		
		Contact contacto = new Contact()
				.name("Joyfe")
				.email("bienvenidos.joyfe@iepgroup.es")
				.url("https://joyfe.iepgroup.es/");
		
		License licencia = new License()
				.name("MIT License")
				.url("https://opensource.org/license/mit/");
		
		Info informacion = new Info().license(licencia)
				.title("API REST JOYFE DES")
				.description("DEW - DES - API REST")
				.termsOfService("https://www.termsfeed.com/public/uploads/2021/12/sample-terms-of-service-template.pdf")
				.contact(contacto)
				.version("1.0");
		
		return new OpenAPI().servers(List.of(devServer, prodServer)).info(informacion);
	}
}
