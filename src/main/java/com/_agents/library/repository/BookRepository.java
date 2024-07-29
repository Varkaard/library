package com._agents.library.repository;

import com._agents.library.entity.Author;
import com._agents.library.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookRepository  extends JpaRepository<Book, Long> {
}
