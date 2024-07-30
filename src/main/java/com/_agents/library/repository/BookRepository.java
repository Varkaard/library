package com._agents.library.repository;

import com._agents.library.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository  extends JpaRepository<Book, Long> {
    List<Book> findByAuthor_Id(Long id);
}
