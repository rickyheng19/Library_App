package com.example.Library.Controller;

import com.example.Library.Services.BookService;
import com.example.Library.domain.DTO.BookDTO;
import com.example.Library.domain.Entities.BookEntity;
import com.example.Library.mappers.Mapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class BookController {

    private Mapper<BookEntity, BookDTO> bookMapper;

    private BookService bookService;

    public BookController(Mapper<BookEntity, BookDTO> bookMapper, BookService bookService) {
        this.bookMapper = bookMapper;
        this.bookService = bookService;
    }

    @PutMapping("/books/{isbn}")
    public ResponseEntity<BookDTO> createBook(@PathVariable("isbn") String isbn, @RequestBody BookDTO bookDTO) {
        BookEntity bookEntity = bookMapper.mapFrom(bookDTO);
        BookEntity savedBookEntity = bookService.createBook(isbn, bookEntity);
        BookDTO savedBookDTO = bookMapper.mapTo(savedBookEntity);
        return new ResponseEntity<>(savedBookDTO, HttpStatus.CREATED);
    }

    @GetMapping("/books")
    public List<BookDTO> listBooks() {
        List<BookEntity> books = bookService.findAll();
        return books.stream().map(bookMapper::mapTo).collect(Collectors.toList());
    }
}
