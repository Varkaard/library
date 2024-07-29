package com._agents.library.controller;

import java.util.List;
import java.util.Optional;

import com._agents.library.exception.MemberAlreadyExistsException;
import com._agents.library.exception.UniqueIdentifierModificationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com._agents.library.entity.Member;
import com._agents.library.exception.MemberNotFoundException;
import com._agents.library.repository.MemberRepository;

@RestController
public class MemberController {

    private final MemberRepository repository;

    MemberController(MemberRepository repository){
        this.repository = repository;
    }

    // Get all members
    @GetMapping("/members")
    List<Member> all() {
        return repository.findAll();
    }

    // Get single Member
    @GetMapping("/members/{username}")
    Member one(@PathVariable String username) {
        return repository.findByUsername(username).orElseThrow(()-> new MemberNotFoundException(username));
    }

    // Create member
    @PostMapping("/members")
    Member newMember(@RequestBody Member newMember){
        if (repository.findByUsername(newMember.getUsername()).isPresent()){
            throw new MemberAlreadyExistsException(newMember.getUsername());
        }
        newMember.validateRequiredAttributes();
        return repository.save(newMember);
    }

    // Update member
    @PatchMapping("/members/{username}")
    Member patchMember(@RequestBody Member newMember,@PathVariable String username) {
        // Check if username from request body is different from the username in the path variable
        if (!newMember.getUsername().isEmpty() && !newMember.getUsername().equals(username)){
            throw new UniqueIdentifierModificationException(newMember.getUsername());
        }
        Member existingMember = repository.findByUsername(username).orElseThrow(()-> new MemberNotFoundException(username));
        if (newMember.getAddress() != null){
            existingMember.setAddress(newMember.getAddress());
        }
        if (newMember.getEmail() != null){
            existingMember.setEmail(newMember.getEmail());
        }
        return repository.save(existingMember);
    }

    // Replace Member
    @PutMapping("/members/{username}")
    Member replaceMember(@RequestBody Member newMember, @PathVariable String username){
        // Check if username from request body is different from the username in the path variable
        if (!newMember.getUsername().equals(username)){
            throw new UniqueIdentifierModificationException(newMember.getUsername());
        }
        newMember.validateRequiredAttributes();
        return repository.findByUsername(username).map(Member -> {
            Member.setEmail(newMember.getEmail());
            Member.setAddress(newMember.getAddress());
            return repository.save(Member);
        }).orElseGet(() -> {
            return repository.save(newMember);
        });
    }

    // Delete Member
    @DeleteMapping("/members/{username}")
    public ResponseEntity<String> deleteMember(@PathVariable String username) {
        Optional<Member> existingMember = repository.findByUsername(username);
        // Check if member exists before deleting it
        if(existingMember.isPresent()){
            repository.deleteByUsername(username);
            return ResponseEntity.ok().build();
        }
        // Send 404 if author wasn't found & deleted
        return ResponseEntity.notFound().build();
    }
}
