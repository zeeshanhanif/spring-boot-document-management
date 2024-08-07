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

import org.modelmapper.ModelMapper;

@Service
public class AuthorService {
    
    private static final Logger log = LoggerFactory.getLogger(AuthorService.class);

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private ModelMapper modelMapper;
    
    public AuthorDTO saveAuthor(AuthorDTO authorDto){
        if(authorDto.getFirstName() == null || authorDto.getLastName() == null){
            throw new AuthorNullValueException("First Name and Last Name must be provided");
        }
        Author authorDB = authorRepository.findByFirstNameAndLastName(authorDto.getFirstName(), authorDto.getLastName());
        if(authorDB != null){
            throw new AuthorAlreadyExistsException("Author with same First and Last name already exists");
        }
        Author author = modelMapper.map(authorDto, Author.class);
        return modelMapper.map(authorRepository.save(author),AuthorDTO.class);
    }

    public List<AuthorDTO> getAllAuthors() {
        return authorRepository.findAll().stream().map(author-> convertEntityToDTO(author)).toList();
    }

    public AuthorDTO getAuthorById(long id) {
        return authorRepository.findById(id).map(author-> convertEntityToDTO(author))
                            .orElseThrow(()-> new AuthorNotFoundException("No Such Author Exists with id "+id));
    }

    public AuthorDTO updateAuthor(long authorId, AuthorDTO authorDto) {
        Author originalAuthor = authorRepository.findById(authorId)
                            .orElseThrow(()-> new AuthorNotFoundException("No Such Author Exists with id "+authorId));

        if (Objects.nonNull(authorDto.getFirstName()) && !"".equalsIgnoreCase(authorDto.getFirstName())) {
            originalAuthor.setFirstName(authorDto.getFirstName());
        }
        if (Objects.nonNull(authorDto.getLastName()) && !"".equalsIgnoreCase(authorDto.getLastName())) {
            originalAuthor.setLastName(authorDto.getLastName());
        }
        if(Objects.nonNull(authorDto.getDocuments()) && authorDto.getDocuments().size()>0) {
            List<Document> documents = authorDto.getDocuments().stream()
                        .map(document-> documentRepository.findById(document.getId())
                        .orElseThrow(()-> new DocumentNotFoundException("No Such Document Exists with id "+document.getId())))
                        .toList();
            
            documents.forEach(document-> {
                document.getAuthors().add(originalAuthor);
                documentRepository.save(document);
            });
        }
        return convertEntityToDTO(authorRepository.save(originalAuthor));
    }


    public boolean deleteAuthorById(long id){
        Author author = authorRepository.findById(id).orElseThrow(()-> new AuthorNotFoundException("No Such Author Exists with id "+id));
        if(author != null){
            authorRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public AuthorDTO convertEntityToDTO(Author authorEntity) {
        List<DocumentDTO> documentDtos = authorEntity.getDocuments().stream()
                .map(document-> {
                    List<ReferenceDTO> referenceDtos = document.getReferences().stream()
                                            .map(reference-> new ReferenceDTO(reference.getId(), reference.getReference())).toList();
                    List<AuthorDTO> authorDtos = document.getAuthors().stream()
                                            .map(author-> new AuthorDTO(author.getId(), author.getFirstName(), author.getLastName())).toList();
                    return new DocumentDTO(document.getId(), document.getTitle(),
                                                document.getBody(), referenceDtos, authorDtos);
                }).toList();
        AuthorDTO authorDto = new AuthorDTO(authorEntity.getId(), authorEntity.getFirstName(), authorEntity.getLastName(),documentDtos);
        return authorDto;
    }
}
