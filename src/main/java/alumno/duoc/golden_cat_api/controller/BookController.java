package alumno.duoc.golden_cat_api.controller;

import org.springframework.web.bind.annotation.*;
import java.util.List;

import alumno.duoc.golden_cat_api.model.Book;
import alumno.duoc.golden_cat_api.repository.BookRepository;

@RestController
@RequestMapping("/api/books")
@CrossOrigin(origins = "*") // Permite peticiones desde frontend
public class BookController {

    private final BookRepository bookRepository;

    public BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    // Obtener todos los libros
    @GetMapping
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    // Obtener libro por ID
    @GetMapping("/{id}")
    public Book getBookById(@PathVariable Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));
    }

    // Crear un nuevo libro
    @PostMapping
    public Book createBook(@RequestBody Book book) {
        return bookRepository.save(book);
    }

    // Actualizar libro existente
    @PutMapping("/{id}")
    public Book updateBook(@PathVariable Long id, @RequestBody Book bookDetails) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        book.setTitle(bookDetails.getTitle());
        book.setAuthor(bookDetails.getAuthor());
        return bookRepository.save(book);
    }

    // Eliminar libro
    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable Long id) {
        bookRepository.deleteById(id);
    }
}
