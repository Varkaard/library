package com._agents.library.controller;

import com._agents.library.entity.Loan;
import com._agents.library.exceptions.LoanNotFoundException;
import com._agents.library.repository.LoanRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/library")
public class LoanController {

    private final LoanRepository repository;

    LoanController(LoanRepository repository){
        this.repository = repository;
    }

    // Get all loans
    @GetMapping("/loans")
    List<Loan> all() {
        return repository.findAll();
    }

    // Get single Loan
    @GetMapping("/loans/{id}")
    Loan one(@PathVariable Long id) {
        return repository.findById(id).orElseThrow(()-> new LoanNotFoundException(id));
    }

    // Create Loan
    @PostMapping("/loans")
    Loan newLoan(@RequestBody Loan newLoan){
        return repository.save(newLoan);
    }

    // Replace Loan
    @PutMapping("/loans/{id}")
    Loan replaceLoan(@RequestBody Loan newLoan, @PathVariable Long id){
        return repository.findById(id).map(Loan -> {
            Loan.setBook(Loan.getBook());
            Loan.setMember(Loan.getMember());
            Loan.setLendDate(Loan.getLendDate());
            Loan.setReturnDate(Loan.getReturnDate());
            return repository.save(Loan);
        }).orElseGet(() -> {
            return repository.save(newLoan);
        });
    }

    // Delete Loan
    @DeleteMapping("/loans/{id}")
    void deleteLoan(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
