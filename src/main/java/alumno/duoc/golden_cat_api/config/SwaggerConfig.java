package alumno.duoc.golden_cat_api.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

// url : {url}/doc/swagger-ui.html

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
        .info(new Info()
        .title("Golden Cat API")
        .version("1.0")
        .description("Documentacion de la API de Zmart"));
    }
}
