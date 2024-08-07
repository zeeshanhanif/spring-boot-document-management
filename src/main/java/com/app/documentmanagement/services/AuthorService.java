package com.app.documentmanagement.services;

import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.documentmanagement.dto.AuthorDTO;
import com.app.documentmanagement.dto.DocumentDTO;
import com.app.documentmanagement.dto.ReferenceDTO;
import com.app.documentmanagement.entities.Author;
import com.app.documentmanagement.entities.Document;
import com.app.documentmanagement.exceptions.AuthorAlreadyExistsException;
import com.app.documentmanagement.exceptions.AuthorNotFoundException;
import com.app.documentmanagement.exceptions.AuthorNullValueException;
import com.app.documentmanagement.exceptions.DocumentNotFoundException;
import com.app.documentmanagement.repositories.AuthorRepository;
import com.app.documentmanagement.repositories.DocumentRepository;

@Service
public class AuthorService {
    
    private static final Logger log = LoggerFactory.getLogger(AuthorService.class);

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private DocumentRepository documentRepository;
    /*
    public Author saveAuthor(Author author){
        return authorRepository.save(author);
    }*/
    
    public String saveAuthor(Author author){
        if(author.getFirstName() == null || author.getLastName() == null){
            throw new AuthorNullValueException("First Name and Last Name must be provided");
        }
        Author authorDB = authorRepository.findByFirstNameAndLastName(author.getFirstName(), author.getLastName());
        if(authorDB != null){
            throw new AuthorAlreadyExistsException("Author with same First and Last name already exists");
        }
        
        if(author.getDocuments() !=null && author.getDocuments().size()>0) {
            List<Document> documents = author.getDocuments().stream()
                        .map(document-> documentRepository.findById(document.getId())
                        .orElseThrow(()-> new DocumentNotFoundException("No Such Document Exists with id "+document.getId())))
                        .toList();
                        
            // Not working
            author.getDocuments().add(documents.get(0));
        }
        authorRepository.save(author);
        return "Success";
        //return authorRepository.save(author);
    }

    public List<AuthorDTO> getAllAuthors() {
        return authorRepository.findAll().stream().map(author-> {
            List<DocumentDTO> documentDtos = author.getDocuments().stream()
                .map(document-> {
                    List<ReferenceDTO> referenceDtos = document.getReferences().stream()
                                            .map(reference-> new ReferenceDTO(reference.getId(),reference.getReference())).toList();
                    return new DocumentDTO(document.getId(),document.getTitle(), document.getBody(),referenceDtos);
                }).toList();
            AuthorDTO authorDto = new AuthorDTO(author.getId(), author.getFirstName(), author.getLastName(),documentDtos);
            return authorDto;
        }).toList();
    }

    public AuthorDTO getAuthorById(long id) {
        return authorRepository.findById(id).map(author-> {
            List<DocumentDTO> documentDtos = author.getDocuments().stream()
                .map(document-> {
                    List<ReferenceDTO> referenceDtos = document.getReferences().stream()
                                            .map(reference-> new ReferenceDTO(reference.getId(),reference.getReference())).toList();
                    return new DocumentDTO(document.getId(),document.getTitle(),
                                                document.getBody(),referenceDtos);
                }).toList();
            AuthorDTO authorDto = new AuthorDTO(author.getId(), author.getFirstName(), author.getLastName(),documentDtos);
            return authorDto;
        }).orElseThrow(()-> new AuthorNotFoundException("No Such Author Exists with id "+id));
    }

    public Author updateAuthor(long authorId, Author author) {
        Author originalAuthor = authorRepository.findById(authorId)
                            .orElseThrow(()-> new AuthorNotFoundException("No Such Author Exists with id "+authorId));

        if (Objects.nonNull(author.getFirstName()) && !"".equalsIgnoreCase(author.getFirstName())) {
            originalAuthor.setFirstName(author.getFirstName());
        }
        if (Objects.nonNull(author.getLastName()) && !"".equalsIgnoreCase(author.getLastName())) {
            originalAuthor.setLastName(author.getLastName());
        }
        if(Objects.nonNull(author.getDocuments()) && author.getDocuments().size()>0) {
            List<Document> documents = author.getDocuments().stream()
                        .map(document-> documentRepository.findById(document.getId())
                        .orElseThrow(()-> new DocumentNotFoundException("No Such Document Exists with id "+document.getId())))
                        .toList();
            
            documents.forEach(document-> {
                document.getAuthors().add(originalAuthor);
                documentRepository.save(document);
            });
        }
        return authorRepository.save(originalAuthor);
    }


    public boolean deleteAuthorById(long id){
        Author author = authorRepository.findById(id).orElseThrow(()-> new AuthorNotFoundException("No Such Author Exists with id "+id));
        if(author != null){
            authorRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
