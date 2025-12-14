package alumno.duoc.golden_cat_api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Test Controller", description = "API para pruebas")
public class HolaController {

    @GetMapping("/")
    public String inicio() {
        return "Aplicación Spring Boot funcionando correctamente 🚀";
    }

    @GetMapping("/hola")
    public String hola() {
        return "¡Hola desde Spring Boot!";
    }
}
