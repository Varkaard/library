package com._agents.library.entity;

import jakarta.persistence.*;

import java.time.LocalDate;


@Entity
@Table(name = "loans")
public class Loan {

    @Id
    @GeneratedValue
    private Long id;
    @OneToOne
    private Book book;
    @ManyToOne
    private Member member;
    private LocalDate lendDate;
    private LocalDate returnDate;

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public LocalDate getLendDate() {
        return lendDate;
    }

    public void setLendDate(LocalDate lendDate) {
        this.lendDate = lendDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }
}
