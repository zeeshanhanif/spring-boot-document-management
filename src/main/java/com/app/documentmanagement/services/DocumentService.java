package com.app.documentmanagement.services;

import java.util.List;
import java.util.Objects;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.documentmanagement.dto.AuthorDTO;
import com.app.documentmanagement.dto.DocumentDTO;
import com.app.documentmanagement.dto.ReferenceDTO;
import com.app.documentmanagement.entities.Author;
import com.app.documentmanagement.entities.Document;
import com.app.documentmanagement.exceptions.AuthorNotFoundException;
import com.app.documentmanagement.exceptions.DocumentNotFoundException;
import com.app.documentmanagement.exceptions.DocumentNullValueException;
import com.app.documentmanagement.repositories.AuthorRepository;
import com.app.documentmanagement.repositories.DocumentRepository;


@Service
public class DocumentService {
    
    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private ModelMapper modelMapper;

    public DocumentDTO saveDocument(DocumentDTO documentDto){
        if(documentDto.getTitle() == null || documentDto.getBody() == null) {
            throw new DocumentNullValueException("Title and body must be provided");
        }
        if(documentDto.getReferences() == null || documentDto.getReferences().size() == 0){
            throw new DocumentNullValueException("References must be provided");
        }
        if(documentDto.getAuthors() != null && documentDto.getAuthors().size() > 0) {
            documentDto.getAuthors().forEach(author-> authorRepository.findById(author.getId())
                        .orElseThrow(()-> new AuthorNotFoundException("No Such Author Exists with id "+author.getId())));
        } else {
            throw new DocumentNullValueException("Authors must be provided");
        }
        Document document = modelMapper.map(documentDto, Document.class);
        return modelMapper.map(documentRepository.save(document),DocumentDTO.class);
    }

    public List<DocumentDTO> getAllDocuments() {
        return documentRepository.findAll().stream().map(document-> convertEntityToDTO(document)).toList();
    }

    public DocumentDTO getDocumentById(long id) {
        return documentRepository.findById(id).map(document-> convertEntityToDTO(document)).orElseThrow(()-> new DocumentNotFoundException("No Such Document Exists with id "+id));
    }

    public DocumentDTO updateDocument(long id, DocumentDTO documentDto) {
        Document originalDocument = documentRepository.findById(id)
                                .orElseThrow(()-> new DocumentNotFoundException("No Such Document Exists with id "+id));
        if (Objects.nonNull(documentDto.getTitle()) && !"".equalsIgnoreCase(documentDto.getTitle())) {
            originalDocument.setTitle(documentDto.getTitle());
        }
        if (Objects.nonNull(documentDto.getBody()) && !"".equalsIgnoreCase(documentDto.getBody())) {
            originalDocument.setBody(documentDto.getBody());
        }
        if(Objects.nonNull(documentDto.getAuthors()) && documentDto.getAuthors().size()>0) {
            List<Author> authors = documentDto.getAuthors().stream()
                        .map(author-> authorRepository.findById(author.getId())
                        .orElseThrow(()-> new AuthorNotFoundException("No Such Author Exists with id "+author.getId())))
                        .toList();
            if(authors != null && authors.size()>0){
                originalDocument.getAuthors().clear();
                originalDocument.getAuthors().addAll(authors);
            }
        }
        return convertEntityToDTO(documentRepository.save(originalDocument));
    }

    public boolean deleteDocumentById(long id){
        Document document = documentRepository.findById(id).orElseThrow(()-> new DocumentNotFoundException("No Such Document Exists with id "+id));
        if(document != null){
            documentRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public DocumentDTO convertEntityToDTO(Document documentEntity) {
        List<AuthorDTO> authorDtos = documentEntity.getAuthors().stream()
                        .map(author -> new AuthorDTO(author.getId(), author.getFirstName(),
                                                    author.getLastName())).toList();
        List<ReferenceDTO> referenceDtos = documentEntity.getReferences().stream()
                        .map(reference -> new ReferenceDTO(reference.getId(),reference.getReference()))
                        .toList();
        DocumentDTO documentDto = new DocumentDTO(documentEntity.getId(), documentEntity.getTitle(),
                                documentEntity.getBody(), referenceDtos, authorDtos);
        return documentDto;
    }
}
