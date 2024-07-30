package com._agents.library.controller;

import java.util.List;
import java.util.Optional;

import com._agents.library.entity.Loan;
import com._agents.library.exception.MemberAlreadyExistsException;
import com._agents.library.exception.RelatedObjectExistsException;
import com._agents.library.exception.UniqueIdentifierModificationException;
import com._agents.library.repository.LoanRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;


import com._agents.library.entity.Member;
import com._agents.library.exception.MemberNotFoundException;
import com._agents.library.repository.MemberRepository;


@RestController
public class MemberController {

    private final MemberRepository memberRepository;
    @Autowired
    private LoanRepository loanRepository;

    MemberController(MemberRepository memberRepository){
        this.memberRepository = memberRepository;
    }

    // Get all members
    @GetMapping("/members")
    List<Member> all() {
        return memberRepository.findAll();
    }

    // Get single Member
    @GetMapping("/members/{username}")
    Member one(@PathVariable String username) {
        return memberRepository.findByUsername(username).orElseThrow(()-> new MemberNotFoundException(username));
    }

    // Create member
    @PostMapping("/members")
    Member newMember(@RequestBody Member newMember){
        if (memberRepository.findByUsername(newMember.getUsername()).isPresent()){
            throw new MemberAlreadyExistsException(newMember.getUsername());
        }
        newMember.validateRequiredAttributes();
        return memberRepository.save(newMember);
    }

    // Update member
    @PatchMapping("/members/{username}")
    Member patchMember(@RequestBody Member newMember,@PathVariable String username) {
        // Check if username from request body is different from the username in the path variable
        if (newMember.getUsername() != null && !newMember.getUsername().equals(username)){
            throw new UniqueIdentifierModificationException(newMember.getUsername());
        }
        Member existingMember = memberRepository.findByUsername(username).orElseThrow(()-> new MemberNotFoundException(username));
        if (newMember.getAddress() != null){
            existingMember.setAddress(newMember.getAddress());
        }
        if (newMember.getEmail() != null){
            existingMember.setEmail(newMember.getEmail());
        }
        return memberRepository.save(existingMember);
    }

    // Replace Member
    @PutMapping("/members/{username}")
    Member replaceMember(@RequestBody Member newMember, @PathVariable String username){
        // Check if username from request body is different from the username in the path variable
        if (!newMember.getUsername().equals(username)){
            throw new UniqueIdentifierModificationException(newMember.getUsername());
        }
        newMember.validateRequiredAttributes();
        return memberRepository.findByUsername(username).map(Member -> {
            Member.setEmail(newMember.getEmail());
            Member.setAddress(newMember.getAddress());
            return memberRepository.save(Member);
        }).orElseGet(() -> memberRepository.save(newMember));
    }

    // Delete Member
    @DeleteMapping("/members/{username}")
    public ResponseEntity<String> deleteMember(@PathVariable String username) {
        Optional<Member> existingMember = memberRepository.findByUsername(username);
        // Check if member exists before deleting it
        if(existingMember.isPresent()){
            // Check if there is an existing loan before deleting
            List<Loan> existingLoansForMember = loanRepository.findByMember_Username(username);
            if (!existingLoansForMember.isEmpty()){
                throw new RelatedObjectExistsException("loan", existingLoansForMember.get(0).getId());
            }
            memberRepository.deleteByUsername(username);
            return ResponseEntity.ok().build();
        }
        // Send 404 if author wasn't found & deleted
        return ResponseEntity.notFound().build();
    }
}
