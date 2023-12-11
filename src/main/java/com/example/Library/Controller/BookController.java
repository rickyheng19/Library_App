package com.example.Library.Controller;

import com.example.Library.Services.BookService;
import com.example.Library.domain.DTO.BookDTO;
import com.example.Library.domain.Entities.BookEntity;
import com.example.Library.mappers.Mapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
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
    public ResponseEntity<BookDTO> createUpdateBook(@PathVariable("isbn") String isbn, @RequestBody BookDTO bookDTO) {
        BookEntity bookEntity = bookMapper.mapFrom(bookDTO); //convert entity to DTO
        boolean bookExist = bookService.isExist(isbn); //check if it exist using isbn in path
        BookEntity savedBookEntity = bookService.createUpdateBook(isbn, bookEntity); //either create/update book from bookDTO info provided in body
        BookDTO savedBookDTO = bookMapper.mapTo(savedBookEntity); //map back to entity
        if(bookExist) { //do one of these
            return new ResponseEntity<>(savedBookDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(savedBookDTO, HttpStatus.CREATED);
        }
    }

    @GetMapping("/books")
    public List<BookDTO> listBooks() {
        List<BookEntity> books = bookService.findAll();
        return books.stream().map(bookMapper::mapTo).collect(Collectors.toList());
    }

    @GetMapping("/books/{isbn}")
    public ResponseEntity<BookDTO> getBook(@PathVariable("isbn") String isbn) {
        Optional<BookEntity> foundBook = bookService.findOne(isbn);
        return foundBook.map(bookEntity -> {
            BookDTO bookDTO = bookMapper.mapTo(bookEntity);
            return new ResponseEntity<>(bookDTO, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
