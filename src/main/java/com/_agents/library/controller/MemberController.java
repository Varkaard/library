package com._agents.library.controller;

import com._agents.library.entity.Member;
import com._agents.library.exceptions.MemberNotFoundException;
import com._agents.library.repository.MemberRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/library")
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

    // Create Member
    @PostMapping("/members")
    Member newMember(@RequestBody Member newMember){
        return repository.save(newMember);
    }

    // Replace Member
    @PutMapping("/members/{username}")
    Member replaceMember(@RequestBody Member newMember, @PathVariable String username){
        return repository.findByUsername(username).map(Member -> {
            Member.setEmail(Member.getEmail());
            Member.setAddress(Member.getAddress());
            return repository.save(Member);
        }).orElseGet(() -> {
            return repository.save(newMember);
        });
    }

    // Delete Member
    @DeleteMapping("/members/{username}")
    void deleteMember(@PathVariable String username) {
        repository.deleteByUsername(username);
    }
}
