package com.app.documentmanagement.services;

import java.util.List;

import com.app.documentmanagement.dto.DocumentDTO;
import com.app.documentmanagement.entities.Document;

/**
 * This {@Code DocumentService} interface provides methods for database operations
 * 
 * @author Zeeshan Hanif
 * @see DocumentRepository
 * @see DocumentServiceImpl
 */
public interface DocumentService {
    
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
    DocumentDTO saveDocument(DocumentDTO documentDto);

    /**
     * This method will return {@Code List} of {@Code DocumentDTO} from database using {@Code DocumentRepository} if 
     * there is no data then it will return empty list. 
     * 
     * @return {@Code List} of {@Code DocumentDTO} that is returned from database repository 
     */
    List<DocumentDTO> getAllDocuments();

    /**
     * This method will return {@Code DocumentDTO} from database using provided id
     * 
     * @param id id of document to be found
     * @return {@Code DocumentDTO} that is returned from database repository
     * @throws DocumentNotFoundException if id provided does not exists
     */
    DocumentDTO getDocumentById(long id);

    /**
     * Update the {@Code Document} data into database using {@Code DocumentRepository} if data provided is valid.
     * 
     * @param id id for {@Code Document} that needs to be updated
     * @param documentDto DTO object that will be updated into database
     * @return {@Code DocumentDTO} that is returned from database repository
     * @throws DocumentNotFoundException if id provided does not exists in database
     * @throws AuthorNotFoundException if any {@Code Author} from the list provided does not exists in database
     */
    DocumentDTO updateDocument(long id, DocumentDTO documentDto);

    /**
     * Delete the {@Code Document} from database using provided id. 
     * 
     * @param id id for {@Code Document} that needs to be deleted
     * @return {@Code true} if {@Code Document} deleted successfully
     * @throws DocumentNotFoundException if id provided does not exists in database
     */
    boolean deleteDocumentById(long id);

    /**
     * Utitlity method to convert {@Code Document} entity to {@Code DocumentDTO} DTO
     * This mehtod will convert all the nested authors {@Code Author} and references {@Code Reference}
     * related to it as well to their respective DTOs
     * 
     * @param documentEntity {@Code Document} that needs to be converted
     * @return {@Code DocumentDTO} after mapping all the properites from {@Code Document}
     */
    DocumentDTO convertEntityToDTO(Document documentEntity);

    /**
     * Utitlity method to convert {@Code DocumentDTO} entity to {@Code Document} DTO
     * This mehtod will convert all the nested authors {@Code AuthorDTO} and references {@Code ReferenceDTO}
     * related to it as well to their respective entities
     * 
     * @param documentDto {@Code DocumentDTO} that needs to be converted
     * @return {@Code Document} after mapping all the properites from {@Code DocumentDTO}
     */
    Document convertToDocumentEntityFromDoucmentDTO(DocumentDTO documentDto);
}
