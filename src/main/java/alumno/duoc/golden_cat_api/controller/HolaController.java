package alumno.duoc.golden_cat_api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
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
