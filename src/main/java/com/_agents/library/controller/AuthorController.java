package com._agents.library.controller;

import com._agents.library.exceptions.AuthorNotFoundException;
import com._agents.library.entity.Author;
import com._agents.library.repository.AuthorRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/library")
public class AuthorController {

    private final AuthorRepository repository;

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
        return repository.save(newAuthor);
    }

    // Replace author
    @PutMapping("/authors/{id}")
    Author replaceAuthor(@RequestBody Author newAuthor, @PathVariable Long id){
        return repository.findById(id).map(author -> {
            author.setName(author.getName());
            author.setDateOfBirth(author.getDateOfBirth());
            return repository.save(author);
        }).orElseGet(() -> {
            return repository.save(newAuthor);
        });
    }

    // Delete author
    @DeleteMapping("/authors/{id}")
    void deleteAuthor(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
