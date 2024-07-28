package com._agents.library.controller;

import com._agents.library.entity.Book;
import com._agents.library.exceptions.BookNotFoundException;
import com._agents.library.repository.BookRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/library")
public class BookController {

    private final BookRepository repository;

    BookController(BookRepository repository){
        this.repository = repository;
    }

    // Get all books
    @GetMapping("/books")
    List<Book> all() {
        return repository.findAll();
    }

    // Get single Book
    @GetMapping("/books/{id}")
    Book one(@PathVariable Long id) {
        return repository.findById(id).orElseThrow(()-> new BookNotFoundException(id));
    }

    // Create Book
    @PostMapping("/books")
    Book newBook(@RequestBody Book newBook){
        return repository.save(newBook);
    }

    // Replace Book
    @PutMapping("/books/{id}")
    Book replaceBook(@RequestBody Book newBook, @PathVariable Long id){
        return repository.findById(id).map(Book -> {
            Book.setTitle(Book.getTitle());
            Book.setGenre(Book.getGenre());
            Book.setPrice(Book.getPrice());
            Book.setAuthor(Book.getAuthor());
            return repository.save(Book);
        }).orElseGet(() -> {
            return repository.save(newBook);
        });
    }

    // Delete Book
    @DeleteMapping("/books/{id}")
    void deleteBook(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
