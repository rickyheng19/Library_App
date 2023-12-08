package com.example.Library.mappers.impl;

import com.example.Library.domain.DTO.BookDTO;
import com.example.Library.domain.Entities.BookEntity;
import com.example.Library.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class BookMapperImpl implements Mapper<BookEntity, BookDTO> {

    private ModelMapper modelMapper;

    public BookMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public BookDTO mapTo(BookEntity bookEntity) {
        return modelMapper.map(bookEntity, BookDTO.class);
    }

    @Override
    public BookEntity mapFrom(BookDTO bookDTO) {
        return modelMapper.map(bookDTO, BookEntity.class);
    }
}
