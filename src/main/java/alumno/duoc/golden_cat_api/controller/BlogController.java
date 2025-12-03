package alumno.duoc.golden_cat_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

import alumno.duoc.golden_cat_api.model.Blog;
import alumno.duoc.golden_cat_api.repository.BlogRepository;

@RestController
@RequestMapping("/api/blog")
@CrossOrigin(origins = "*")
@Tag(name = "Blog Controller", description = "API para gestionar operaciones CRUD de blogs")
public class BlogController {

    @Autowired
    private final BlogRepository blogRepository;

    // Inyección por constructor
    public BlogController(BlogRepository blogRepository) {
        this.blogRepository = blogRepository;
    }

    // Obtener todos los blogs
    @GetMapping
    @Operation(
        summary = "Obtener todos los blogs", 
        description = "Retorna una lista de todos los blogs en el sistema con paginación opcional"
    )
    public List<Blog> getAllBlogs() {
        return blogRepository.findAll();
    }

    // Obtener blog por ID
    @GetMapping("/{id}")
    @Operation(
        summary = "Obtener blog por ID", 
        description = "Retorna un blog específico a partir de su ID"
    )
    public ResponseEntity<Blog> getBlogById(@PathVariable Long id) {
        return blogRepository.findById(id)
                .map(blog -> ResponseEntity.ok(blog))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Crear un nuevo blog
    @PostMapping
    @Operation(
        summary = "Crear nuevo blog", 
        description = "Registra un nuevo blog en el sistema y lo retorna"
    )
    public ResponseEntity<Blog> createBlog(@RequestBody Blog blog) {
        Blog savedBlog = blogRepository.save(blog);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBlog);
    }

    // Actualizar blog existente
    @PutMapping("/{id}")
    @Operation(
        summary = "Actualizar blog", 
        description = "Actualiza la información de un blog existente por su ID"
    )
    public ResponseEntity<Blog> updateBlog(@PathVariable Long id, @RequestBody Blog blogDetails) {
        return blogRepository.findById(id)
                .map(existingBlog -> {
                    existingBlog.setNombre(blogDetails.getNombre());
                    existingBlog.setDescription(blogDetails.getDescription());
                    existingBlog.setBody(blogDetails.getBody());
                    existingBlog.setDate(blogDetails.getDate());
                    existingBlog.setWriter(blogDetails.getWriter());
                    
                    Blog updatedBlog = blogRepository.save(existingBlog);
                    return ResponseEntity.ok(updatedBlog);
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Eliminar blog
    @DeleteMapping("/{id}")
    @Operation(
        summary = "Eliminar blog", 
        description = "Elimina un blog del sistema por su ID"
    )
    public ResponseEntity<Void> deleteBlog(@PathVariable Long id) {
        if (blogRepository.existsById(id)) {
            blogRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // Buscar blogs por escritor
    @GetMapping("/writer/{writer}")
    @Operation(
        summary = "Buscar blogs por escritor", 
        description = "Retorna una lista de blogs escritos por un autor específico"
    )
    public ResponseEntity<List<Blog>> getBlogsByWriter(@PathVariable String writer) {
        List<Blog> blogs = blogRepository.findByWriter(writer);
        if (blogs.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(blogs);
    }

    // Buscar blogs por palabra clave en nombre
    @GetMapping("/search/nombre/{keyword}")
    @Operation(
        summary = "Buscar blogs por palabra clave en el nombre", 
        description = "Retorna blogs cuyo nombre contiene la palabra clave especificada"
    )
    public List<Blog> searchBlogsByNombre(@PathVariable String keyword) {
        return blogRepository.findByNombreContaining(keyword);
    }

    // Buscar blogs por palabra clave en descripción
    @GetMapping("/search/description/{keyword}")
    @Operation(
        summary = "Buscar blogs por palabra clave en la descripción", 
        description = "Retorna blogs cuya descripción contiene la palabra clave especificada"
    )
    public List<Blog> searchBlogsByDescription(@PathVariable String keyword) {
        return blogRepository.findByDescriptionContaining(keyword);
    }

    // Obtener blogs recientes (últimos 10)
    @GetMapping("/recent")
    @Operation(
        summary = "Obtener blogs recientes", 
        description = "Retorna los 10 blogs más recientes ordenados por fecha"
    )
    public List<Blog> getRecentBlogs() {
        return blogRepository.findAll().stream()
                .sorted((b1, b2) -> b2.getDate().compareTo(b1.getDate()))
                .limit(10)
                .toList();
    }

    // Contar total de blogs
    @GetMapping("/count")
    @Operation(
        summary = "Contar blogs", 
        description = "Retorna el número total de blogs en el sistema"
    )
    public Long countBlogs() {
        return blogRepository.count();
    }
}