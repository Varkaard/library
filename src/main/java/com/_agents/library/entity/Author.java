package com._agents.library.entity;

import java.time.LocalDate;
import java.util.List;

import com._agents.library.exception.RequiredDataMissingException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;


@Entity
@Table(name = "authors")
public class Author {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private LocalDate dateOfBirth;
    @OneToMany(
            mappedBy = "author"//,
           // cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}
    )
    @JsonIgnore
    private List<Book> books;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public void validateRequiredAttributes(){
        // Check for required data
        if (this.getName() == null){
            throw new RequiredDataMissingException("name");
        }
        if (this.getDateOfBirth()== null){
            throw new RequiredDataMissingException("date of birth");
        }
    }
}