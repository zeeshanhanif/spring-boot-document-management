package com.app.documentmanagement.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.documentmanagement.dto.AuthorDTO;
import com.app.documentmanagement.dto.DocumentDTO;
import com.app.documentmanagement.entities.Author;
import com.app.documentmanagement.entities.Document;
import com.app.documentmanagement.repositories.AuthorRepository;
import com.app.documentmanagement.repositories.DocumentRepository;

@Service
public class AuthorService {
    
    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private DocumentRepository documentRepository;

    public Author saveAuthor(Author author){
        return authorRepository.save(author);
    }

    public List<AuthorDTO> getAllAuthors() {
        return authorRepository.findAll().stream().map(author-> {
            List<DocumentDTO> documentDtos = author.getDocuments().stream()
                .map(document-> new DocumentDTO(document.getId(),document.getTitle(),
                                                document.getBody(),document.getReferences())).toList();
            AuthorDTO authorDto = new AuthorDTO(author.getId(), author.getFirstName(), author.getLastName(),documentDtos);
            return authorDto;
        }).toList();
        
    }

    public AuthorDTO getAuthorById(long id) {
        return authorRepository.findById(id).map(author-> {
            List<DocumentDTO> documentDtos = author.getDocuments().stream()
                .map(document-> new DocumentDTO(document.getId(),document.getTitle(),
                                                document.getBody(),document.getReferences())).toList();
            AuthorDTO authorDto = new AuthorDTO(author.getId(), author.getFirstName(), author.getLastName(),documentDtos);
            return authorDto;
        }).orElse(null);
    }
/*
    Author getAuthorEntityById(long id) {
        return authorRepository.findById(id).orElse(null);
    }
*/
    public Author updateAuthor(long authorId, Author author) {
        Author originalAuthor = authorRepository.findById(authorId).orElse(null);
        System.out.println("update author = "+author);

        if(originalAuthor != null) {
            if (Objects.nonNull(author.getFirstName()) && !"".equalsIgnoreCase(author.getFirstName())) {
                originalAuthor.setFirstName(author.getFirstName());
            }
            if (Objects.nonNull(author.getLastName()) && !"".equalsIgnoreCase(author.getLastName())) {
                originalAuthor.setLastName(author.getLastName());
            }
            if(Objects.nonNull(author.getDocuments()) && author.getDocuments().size()>0) {
                Document doc = documentRepository.findById(Long.valueOf(2)).orElse(null);
                doc.getAuthors().add(originalAuthor);
                documentRepository.save(doc);

                
                List<Document> documents = author.getDocuments().stream()
                            .map(document-> documentRepository.findById(document.getId()).orElse(null)).toList();
                documents.
                //originalAuthor.setDocuments(documents);

            }
            return authorRepository.save(originalAuthor);
        }
        return null;
    }


    public boolean deleteAuthorById(long id){
        Author author = authorRepository.findById(id).orElse(null);
        if(author != null){
            authorRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
