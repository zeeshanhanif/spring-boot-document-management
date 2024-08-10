package com.app.documentmanagement.services;

import java.util.List;

import com.app.documentmanagement.dto.AuthorDTO;
import com.app.documentmanagement.entities.Author;

public interface AuthorService {
    AuthorDTO saveAuthor(AuthorDTO authorDto);
    List<AuthorDTO> getAllAuthors();
    AuthorDTO getAuthorById(long id);
    AuthorDTO updateAuthor(long authorId, AuthorDTO authorDto);
    boolean deleteAuthorById(long id);
    AuthorDTO convertEntityToDTO(Author authorEntity);
}
