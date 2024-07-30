package com._agents.library.controller;

import java.util.List;
import java.util.Optional;

import com._agents.library.entity.Book;
import com._agents.library.entity.Member;
import com._agents.library.exception.*;
import com._agents.library.repository.BookRepository;
import com._agents.library.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com._agents.library.entity.Loan;
import com._agents.library.repository.LoanRepository;

@RestController
public class LoanController {

    private final LoanRepository loanRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private MemberRepository memberRepository;

    LoanController(LoanRepository loanRepository){
        this.loanRepository = loanRepository;
    }

    // Get all loans
    @GetMapping("/loans")
    List<Loan> all() {
        return loanRepository.findAll();
    }

    // Get single Loan
    @GetMapping("/loans/{id}")
    Loan one(@PathVariable Long id) {
        return loanRepository.findById(id).orElseThrow(()-> new LoanNotFoundException(id));
    }

    // Create Loan
    @PostMapping("/loans")
    Loan newLoan(@RequestBody Loan newLoan, @RequestParam(name="member username", required=true) String memberUsername, @RequestParam(name="book id", required = true) Long bookId){
        // Check if book exists and is already loaned
        Book existingBook = bookRepository.findById(bookId).orElseThrow(()-> new BookNotFoundException(bookId));
        if (loanRepository.findByBook_Id(existingBook.getId()).isPresent()){
            throw new LoanAlreadyExistsException(existingBook.getId());
        }
        // Check if member exists
        Member existingMember = memberRepository.findByUsername(memberUsername).orElseThrow(()-> new MemberNotFoundException(memberUsername));
        // Check for maximum amount of loans per member
        List<Loan> loanList = loanRepository.findByMember_Username(memberUsername);
        if (loanList.size() > 4){
            throw new MaximumLoansReachedException(loanList.size()+1);
        }
        newLoan.validateRequiredAttributes();
        newLoan.setMember(existingMember);
        newLoan.setBook(existingBook);
        return loanRepository.save(newLoan);
    }

    // Update loan
    @PatchMapping("/loan/{id}")
    Loan patchLoan(@RequestBody Loan newLoan, @PathVariable Long id, @RequestParam(name="member username", required=false) String memberUsername, @RequestParam(name="book id", required = false) Long bookId) {
        // Check if id from request body is different from the id in the path variable
        if (newLoan.getId() != null && !newLoan.getId().equals(id)){
            throw new UniqueIdentifierModificationException(newLoan.getId());
        }
        Loan existingLoan = loanRepository.findById(id).orElseThrow(()-> new LoanNotFoundException(id));
        // Check if given member exists
        if (memberUsername != null){
            Optional<Member> existingMember = memberRepository.findByUsername(memberUsername);
            if (existingMember.isPresent()){
                existingLoan.setMember(existingMember.get());
            }else {
                throw new MemberNotFoundException(memberUsername);
            }
        }
        // Check if given book exists
        if (bookId != null){
            Optional<Book> existingBook = bookRepository.findById(bookId);
            if (existingBook.isPresent()){
                existingLoan.setBook(existingBook.get());
            }else {
                throw new BookNotFoundException(bookId);
            }
        }

        if (newLoan.getLendDate() != null){
            existingLoan.setLendDate(newLoan.getLendDate());
        }
        if (newLoan.getReturnDate() != null){
            existingLoan.setReturnDate(newLoan.getReturnDate());
        }
        return loanRepository.save(existingLoan);
    }

    // Replace Loan
    @PutMapping("/loans/{id}")
    Loan replaceLoan(@RequestBody Loan newLoan, @PathVariable Long id, @RequestParam(name="member username", required=true) String memberUsername, @RequestParam(name="book id", required = true) Long bookId){
        // Check if id from request body is different from the id in the path variable
        if (newLoan.getId() != null && !newLoan.getId().equals(id)){
            throw new UniqueIdentifierModificationException(newLoan.getId());
        }
        newLoan.validateRequiredAttributes();

        // Check if given member exists
        Optional<Member> existingMember = memberRepository.findByUsername(memberUsername);
        if (existingMember.isPresent()){
            newLoan.setMember(existingMember.get());
        }else {
            throw new MemberNotFoundException(memberUsername);
        }
        // Check if given book exists
        Optional<Book> existingBook = bookRepository.findById(bookId);
        if (existingBook.isPresent()){
            newLoan.setBook(existingBook.get());
        }else {
            throw new BookNotFoundException(bookId);
        }

        return loanRepository.findById(id).map(Loan -> {
            Loan.setBook(newLoan.getBook());
            Loan.setMember(newLoan.getMember());
            Loan.setLendDate(newLoan.getLendDate());
            Loan.setReturnDate(newLoan.getReturnDate());
            return loanRepository.save(Loan);
        }).orElseGet(() -> {
            // Check for maximum amount of loans per member
            List<Loan> loanList = loanRepository.findByMember_Username(memberUsername);
            if (loanList.size() > 4){
                throw new MaximumLoansReachedException(loanList.size()+1);
            }
            return loanRepository.save(newLoan);
        });
    }

    // Delete Loan
    @DeleteMapping("/loans/{id}")
    public ResponseEntity<String> deleteLoan(@PathVariable Long id) {
        Optional<Loan> existingLoan = loanRepository.findById(id);
        // Check if member exists before deleting it
        if(existingLoan.isPresent()){
            loanRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        // Send 404 if author wasn't found & deleted
        return ResponseEntity.notFound().build();
    }
}
