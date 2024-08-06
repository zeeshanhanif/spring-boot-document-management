package com.app.documentmanagement.services;

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
public class DocumentService {
    
    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private AuthorRepository authorRepository;

    public Document saveDocument(Document document){
        return documentRepository.save(document);
    }

    public List<DocumentDTO> getAllDocuments() {
        return documentRepository.findAll().stream().map(document-> {
            List<AuthorDTO> authorDtos = document.getAuthors().stream()
                        .map(author -> new AuthorDTO(author.getId(), author.getFirstName(),
                                                    author.getLastName())).toList();
            DocumentDTO documentDto = new DocumentDTO(document.getId(), document.getTitle(),
                            document.getBody(), document.getReferences(), authorDtos);
            return documentDto;
        })
        .toList();
    }

    public DocumentDTO getDocumentById(long id) {
        return documentRepository.findById(id).map(document-> {
            List<AuthorDTO> authorDtos = document.getAuthors().stream()
                    .map(author -> new AuthorDTO(author.getId(), author.getFirstName(),
                                                author.getLastName())).toList();
            DocumentDTO documentDto = new DocumentDTO(document.getId(), document.getTitle(),
            document.getBody(), document.getReferences(), authorDtos);
            return documentDto;
        }).orElse(null);
    }
/*
    Document getDocumentEntityById(long id) {
        return documentRepository.findById(id).orElse(null);
    }
 */
    public Document updateDocument(long id, Document document) {
        Document originalDocument = documentRepository.findById(id).orElse(null);
        if(originalDocument != null){
            if (Objects.nonNull(document.getTitle()) && !"".equalsIgnoreCase(document.getTitle())) {
                originalDocument.setTitle(document.getTitle());
            }
            if (Objects.nonNull(document.getBody()) && !"".equalsIgnoreCase(document.getBody())) {
                originalDocument.setBody(document.getBody());
            }
            if(Objects.nonNull(document.getAuthors()) && document.getAuthors().size()>0) {
                List<Author> authors = document.getAuthors().stream()
                            .map(author-> authorRepository.findById(author.getId()).orElse(null)).toList();
                if(authors != null && authors.size()>0){
                    originalDocument.getAuthors().clear();
                    originalDocument.getAuthors().addAll(authors);
                }
            }
            return documentRepository.save(originalDocument);
        }
        return null;
    }

    public boolean deleteDocumentById(long id){
        Document document = documentRepository.findById(id).orElse(null);
        if(document != null){
            documentRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
