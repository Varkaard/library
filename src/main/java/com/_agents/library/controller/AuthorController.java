package com._agents.library.controller;

import java.util.List;
import java.util.Optional;

import com._agents.library.entity.Book;
import com._agents.library.exception.RelatedObjectExistsException;
import com._agents.library.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com._agents.library.entity.Author;
import com._agents.library.exception.AuthorNotFoundException;
import com._agents.library.repository.AuthorRepository;

@RestController
public class AuthorController {

    private final AuthorRepository authorRepository;
    @Autowired
    private BookRepository bookRepository;

    AuthorController(AuthorRepository repository){
        this.authorRepository = repository;
    }

    // Get all authors
    @GetMapping("/authors")
    List<Author> all() {
        return authorRepository.findAll();
    }

    // Get single author
    @GetMapping("/authors/{id}")
    Author one(@PathVariable Long id) {
        return authorRepository.findById(id).orElseThrow(()-> new AuthorNotFoundException(id));
    }

    // Create author
    @PostMapping("/authors")
    Author newAuthor(@RequestBody Author newAuthor){
        newAuthor.validateRequiredAttributes();
        return authorRepository.save(newAuthor);
    }

    // Update author
    @PatchMapping("/authors/{id}")
    Author patchAuthor(@RequestBody Author newAuthor, @PathVariable Long id) {
        Author existingAuthor = authorRepository.findById(id).orElseThrow(()-> new AuthorNotFoundException(id));
        if (newAuthor.getName() != null){
            existingAuthor.setName(newAuthor.getName());
        }
        if (newAuthor.getDateOfBirth() != null){
            existingAuthor.setDateOfBirth(newAuthor.getDateOfBirth());
        }
        return authorRepository.save(existingAuthor);
    }

    // Replace author
    @PutMapping("/authors/{id}")
    Author replaceAuthor(@RequestBody Author newAuthor, @PathVariable Long id) {
        authorRepository.findById(id).orElseThrow(()-> new AuthorNotFoundException(id));
        newAuthor.validateRequiredAttributes();
        return authorRepository.findById(id).map(author -> {
            author.setName(newAuthor.getName());
            author.setDateOfBirth(newAuthor.getDateOfBirth());
            return authorRepository.save(author);
        }).orElseGet(() -> authorRepository.save(newAuthor));
    }

    // Delete author
    @DeleteMapping("/authors/{id}")
    public ResponseEntity<String> deleteAuthor(@PathVariable Long id) {
        Optional<Author> existingAuthor = authorRepository.findById(id);
        // Check if author exists before deleting it
        if(existingAuthor.isPresent()){
            // Check if there is an existing book before deleting
            List<Book> existingBooksForAuthor = bookRepository.findByAuthor_Id(id);
            if (!existingBooksForAuthor.isEmpty()){
                throw new RelatedObjectExistsException("book", existingBooksForAuthor.get(0).getId());
            }
            authorRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        // Send 404 if author wasn't found & deleted
        return ResponseEntity.notFound().build();
    }
}
