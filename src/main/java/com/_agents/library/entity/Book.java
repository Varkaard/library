package com._agents.library.entity;

import com._agents.library.exception.RequiredDataMissingException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue
    private Long id;
    private String title;
    private String genre;
    private BigDecimal price;
    @ManyToOne
    @JoinColumn(name = "author_id")
    @JsonIgnore
    private Author author;
    @OneToOne
    @JsonIgnore
    private Loan loan;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Loan getLoan() {
        return loan;
    }

    public void setLoan(Loan loan) {
        this.loan = loan;
    }

    public void validateRequiredAttributes(){
        // Check for required data
        if (this.getTitle() == null){
            throw new RequiredDataMissingException("title");
        }
        if (this.getGenre()== null){
            throw new RequiredDataMissingException("genre");
        }
        if (this.getPrice()== null){
            throw new RequiredDataMissingException("price");
        }
    }
}
