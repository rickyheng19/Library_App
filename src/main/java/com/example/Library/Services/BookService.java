package com.example.Library.Services;

import com.example.Library.domain.Entities.BookEntity;

public interface BookService {

    BookEntity createBook(String isbn, BookEntity book);
}
