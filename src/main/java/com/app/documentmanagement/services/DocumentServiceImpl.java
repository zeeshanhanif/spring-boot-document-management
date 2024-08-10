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
import com.app.documentmanagement.entities.Reference;
import com.app.documentmanagement.exceptions.AuthorNotFoundException;
import com.app.documentmanagement.exceptions.DocumentNotFoundException;
import com.app.documentmanagement.exceptions.DocumentNullValueException;
import com.app.documentmanagement.repositories.AuthorRepository;
import com.app.documentmanagement.repositories.DocumentRepository;

/**
 * This {@Code DocumentServiceImpl} class provides implementation for the methods {@Code DocumentService}.
 * {@Code DocumentServiceImpl} will to interact with database repository to perform database operation.
 * Also this class will act as Service for Spring Boot framework
 * 
 * @author Zeeshan Hanif
 * @see DocumentRepository
 * @see DocumentService 
 */
@Service
public class DocumentServiceImpl implements DocumentService{
    
    /**
     * {@Code DocumentRepository} to interact with document table of database.
     * It will be auto wired by spring boot framework
     */
    @Autowired
    private DocumentRepository documentRepository;

    /**
     * {@Code AuthorRepository} to interact with author table of database.
     * It will be auto wired by spring boot framework
     */
    @Autowired
    private AuthorRepository authorRepository;

    /**
     * {@Code ModelMapper} to map object properties from database entities to data transfer objects
     * It will be auto wired by spring boot framework
     */
    @Autowired
    private ModelMapper modelMapper;

    /**
     * This method will save {@Code Document} data into database using {@Code DocumentRepository} if 
     * data provided is valid. 
     * Returns {@Code DocumentDTO} after saving data into database. If the any of the property is not 
     * provided, then there will be an error 
     * 
     * @param documentDto {@Code DocumentDTO} object that will be saved into database
     * @return {@Code DocumentDTO} that is returned from database repository
     * @throws DocumentNullValueException if any of the title, body, references or authors are null or empty
     * @throws AuthorNotFoundException if any of authors provided does not exists in system
     */
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
        Document document = convertToDocumentEntityFromDoucmentDTO(documentDto);
        return convertEntityToDTO(documentRepository.save(document));

        // TODO: findout later on why modelMapper injection not working in test cases
        //Document document = modelMapper.map(documentDto, Document.class);
        //return modelMapper.map(documentRepository.save(document),DocumentDTO.class);
    }

    /**
     * This method will return {@Code List} of {@Code DocumentDTO} from database using {@Code DocumentRepository} if 
     * there is no data then it will return empty list. 
     * 
     * @return {@Code List} of {@Code DocumentDTO} that is returned from database repository 
     */
    public List<DocumentDTO> getAllDocuments() {
        return documentRepository.findAll().stream().map(document-> convertEntityToDTO(document)).toList();
    }

    /**
     * This method will return {@Code DocumentDTO} from database using provided id
     * 
     * @param id id of document to be found
     * @return {@Code DocumentDTO} that is returned from database repository
     * @throws DocumentNotFoundException if id provided does not exists
     */
    public DocumentDTO getDocumentById(long id) {
        return documentRepository.findById(id).map(document-> convertEntityToDTO(document)).orElseThrow(()-> new DocumentNotFoundException("No Such Document Exists with id "+id));
    }

    /**
     * Update the {@Code Document} data into database using {@Code DocumentRepository} if data provided is valid.
     * 
     * @param id id for {@Code Document} that needs to be updated
     * @param documentDto DTO object that will be updated into database
     * @return {@Code DocumentDTO} that is returned from database repository
     * @throws DocumentNotFoundException if id provided does not exists in database
     * @throws AuthorNotFoundException if any {@Code Author} from the list provided does not exists in database
     */
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

    /**
     * Delete the {@Code Document} from database using provided id. 
     * 
     * @param id id for {@Code Document} that needs to be deleted
     * @return {@Code true} if {@Code Document} deleted successfully
     * @throws DocumentNotFoundException if id provided does not exists in database
     */
    public boolean deleteDocumentById(long id){
        Document document = documentRepository.findById(id).orElseThrow(()-> new DocumentNotFoundException("No Such Document Exists with id "+id));
        if(document != null){
            documentRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    /**
     * Utitlity method to convert {@Code Document} entity to {@Code DocumentDTO} DTO
     * This mehtod will convert all the nested authors {@Code Author} and references {@Code Reference}
     * related to it as well to their respective DTOs
     * 
     * @param documentEntity {@Code Document} that needs to be converted
     * @return {@Code DocumentDTO} after mapping all the properites from {@Code Document}
     */
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

    /**
     * Utitlity method to convert {@Code DocumentDTO} entity to {@Code Document} DTO
     * This mehtod will convert all the nested authors {@Code AuthorDTO} and references {@Code ReferenceDTO}
     * related to it as well to their respective entities
     * 
     * @param documentDto {@Code DocumentDTO} that needs to be converted
     * @return {@Code Document} after mapping all the properites from {@Code DocumentDTO}
     */
    public Document convertToDocumentEntityFromDoucmentDTO(DocumentDTO documentDto) {
        List<Reference> references = documentDto.getReferences().stream()
                            .map(referenceDto -> new Reference(referenceDto.getId(),referenceDto.getReference()))
                            .toList();
        List<Author> authors = documentDto.getAuthors().stream()
                            .map(authorDto -> new Author(authorDto.getId(),authorDto.getFirstName(),authorDto.getLastName()))
                            .toList();
        return new Document(documentDto.getId(),documentDto.getTitle(),documentDto.getBody(),references,authors);
    }
}
