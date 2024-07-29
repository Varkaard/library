package com._agents.library.repository;

import com._agents.library.entity.Member;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUsername(String username);
    @Modifying
    @Transactional
    @Query(value = "delete from Member m where m.username = ?1")
    void deleteByUsername(String username);
}
