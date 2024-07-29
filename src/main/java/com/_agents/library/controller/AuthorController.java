package com._agents.library.controller;

import java.util.List;
import java.util.Optional;

import com._agents.library.exception.UniqueIdentifierModificationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com._agents.library.entity.Author;
import com._agents.library.exception.AuthorNotFoundException;
import com._agents.library.repository.AuthorRepository;

@RestController
public class AuthorController {

    private AuthorRepository repository;

    AuthorController(AuthorRepository repository){
        this.repository = repository;
    }

    // Get all authors
    @GetMapping("/authors")
    List<Author> all() {
        return repository.findAll();
    }

    // Get single author
    @GetMapping("/authors/{id}")
    Author one(@PathVariable Long id) {
        return repository.findById(id).orElseThrow(()-> new AuthorNotFoundException(id));
    }

    // Create author
    @PostMapping("/authors")
    Author newAuthor(@RequestBody Author newAuthor){
        newAuthor.validateRequiredAttributes();
        return repository.save(newAuthor);
    }

    // Update author
    @PatchMapping("/authors/{id}")
    Author patchAuthor(@RequestBody Author newAuthor, @PathVariable Long id) {
        // Check if id from request body is different from the id in the path variable
        if (newAuthor.getId() != null && !newAuthor.getId().equals(id)){
            throw new UniqueIdentifierModificationException(newAuthor.getId());
        }
        Author existingAuthor = repository.findById(id).orElseThrow(()-> new AuthorNotFoundException(id));
        if (newAuthor.getName() != null){
            existingAuthor.setName(newAuthor.getName());
        }
        if (newAuthor.getDateOfBirth() != null){
            existingAuthor.setDateOfBirth(newAuthor.getDateOfBirth());
        }
        return repository.save(existingAuthor);
    }

    // Replace author
    @PutMapping("/authors/{id}")
    Author replaceAuthor(@RequestBody Author newAuthor, @PathVariable Long id) {
        // Check if id from request body is different from the id in the path variable
        if (newAuthor.getId() != null && !newAuthor.getId().equals(id)){
            throw new UniqueIdentifierModificationException(newAuthor.getId());
        }
        newAuthor.validateRequiredAttributes();
        return repository.findById(id).map(author -> {
            author.setName(newAuthor.getName());
            author.setDateOfBirth(newAuthor.getDateOfBirth());
            return repository.save(author);
        }).orElseGet(() -> {
            return repository.save(newAuthor);
        });
    }

    // Delete author
    @DeleteMapping("/authors/{id}")
    public ResponseEntity<String> deleteAuthor(@PathVariable Long id) {
        Optional<Author> existingAuthor = repository.findById(id);
        // Check if author exists before deleting it
        if(existingAuthor.isPresent()){
            repository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        // Send 404 if author wasn't found & deleted
        return ResponseEntity.notFound().build();
    }
}
