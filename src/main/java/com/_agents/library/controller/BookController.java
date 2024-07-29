package com._agents.library.controller;

import java.util.List;

import com._agents.library.entity.Author;
import com._agents.library.entity.Book;
import com._agents.library.exception.*;
import com._agents.library.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com._agents.library.exception.BookNotFoundException;
import com._agents.library.repository.BookRepository;

@RestController
public class BookController {

    private final BookRepository bookRepository;
    @Autowired
    private AuthorRepository authorRepository;

    BookController(BookRepository bookRepository){
        this.bookRepository = bookRepository;
    }

    // Get all books
    @GetMapping("/books")
    List<Book> all() {
        return bookRepository.findAll();
    }

    // Get single Book
    @GetMapping("/books/{id}")
    Book one(@PathVariable Long id) {
        return bookRepository.findById(id).orElseThrow(()-> new BookNotFoundException(id));
    }

    // Create Book
    @PostMapping("/books")
    Book newBook(@RequestBody Book newBook, @RequestParam(name="author id") Long authorId){
        // Check if given author exists
        Author existingAuthor = authorRepository.findById(authorId).orElseThrow(()-> new AuthorNotFoundException(authorId));
        newBook.validateRequiredAttributes();
        newBook.setAuthor(existingAuthor);
        return bookRepository.save(newBook);
    }

    // Update book
    @PatchMapping("/book/{id}")
    Book patchBook(@RequestBody Book newBook, @PathVariable Long id, @RequestParam(name="author id", required=false) Long authorId) {
        // Check if id from request body is different from the id in the path variable
        if (newBook.getId() != null && !newBook.getId().equals(id)){
            throw new UniqueIdentifierModificationException(newBook.getId());
        }
        Book existingBook = bookRepository.findById(id).orElseThrow(()-> new BookNotFoundException(id));
        if (authorId != null){
            // Check if given author exists
            Author existingAuthor = authorRepository.findById(authorId).orElseThrow(()-> new AuthorNotFoundException(authorId));
            existingBook.setAuthor(existingAuthor);
        }
        if (newBook.getTitle() != null){
            existingBook.setTitle(newBook.getTitle());
        }
        if (newBook.getGenre() != null){
            existingBook.setGenre(newBook.getGenre());
        }
        if (newBook.getPrice() != null){
            existingBook.setPrice(newBook.getPrice());
        }
        return bookRepository.save(existingBook);
    }

    // Replace Book
    @PutMapping("/books/{id}")
    Book replaceBook(@RequestBody Book newBook, @PathVariable Long id, @RequestParam(name="author id", required=true) Long authorId){
        // Check if id from request body is different from the id in the path variable
        if (newBook.getId() != null && !newBook.getId().equals(id)){
            throw new UniqueIdentifierModificationException(newBook.getId());
        }
        // Get author information from existing book
        Author existingAuthor = authorRepository.findById(authorId).orElseThrow(()-> new AuthorNotFoundException(authorId));
        newBook.setAuthor(existingAuthor);
        newBook.validateRequiredAttributes();
        return bookRepository.findById(id).map(Book -> {
            Book.setTitle(newBook.getTitle());
            Book.setGenre(newBook.getGenre());
            Book.setPrice(newBook.getPrice());
            Book.setAuthor(newBook.getAuthor());
            return bookRepository.save(Book);
        }).orElseGet(() -> {
            return bookRepository.save(newBook);
        });
    }

    // Delete Book
    @DeleteMapping("/books/{id}")
    void deleteBook(@PathVariable Long id) {
        bookRepository.deleteById(id);
    }
}
