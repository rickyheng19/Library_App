package com.example.Library.Services;

import com.example.Library.domain.Entities.AuthorEntity;
import org.springframework.stereotype.Component;

@Component
public interface AuthorService {
    AuthorEntity createAuthor(AuthorEntity authorEntity);
}
