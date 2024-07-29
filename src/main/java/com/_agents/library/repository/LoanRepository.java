package com._agents.library.repository;

import com._agents.library.entity.Book;
import com._agents.library.entity.Loan;
import com._agents.library.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    Optional<Loan> findByBook_Id(Long id);
    List<Loan> findByMember_Username(String username);
}
