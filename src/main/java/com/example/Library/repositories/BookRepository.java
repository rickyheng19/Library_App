package com.example.Library.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Library.domain.Entities.BookEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, String>  {
}
