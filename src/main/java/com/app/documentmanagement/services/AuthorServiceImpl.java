package com.app.documentmanagement.services;

import java.util.List;
import java.util.ArrayList;
import java.util.Objects;

import org.modelmapper.ModelMapper;
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
import com.app.documentmanagement.exceptions.DocumentAttachedToAuthorException;
import com.app.documentmanagement.exceptions.DocumentNotFoundException;
import com.app.documentmanagement.repositories.AuthorRepository;
import com.app.documentmanagement.repositories.DocumentRepository;

/**
 * This {@Code AuthorServiceImpl} class provides implementation for the methods {@Code AuthorService}.
 * {@Code AuthorServiceImpl} will to interact with database repository to perform database operation.
 * Also this class will act as Service for Spring Boot framework
 * 
 * @author Zeeshan Hanif
 * @see AuthorRepository
 * @see AuthorService 
 */
@Service
public class AuthorServiceImpl implements AuthorService{
    
    private static final Logger log = LoggerFactory.getLogger(AuthorService.class);

    /**
     * {@Code AuthorRepository} to interact with author table of database.
     * It will be auto wired by spring boot framework
     */
    @Autowired
    private AuthorRepository authorRepository;

    /**
     * {@Code DocumentRepository} to interact with document table of database
     * It will be auto wired by spring boot framework
     */
    @Autowired
    private DocumentRepository documentRepository;

    /**
     * {@Code ModelMapper} to map object properties from database entities to data transfer objects
     * It will be auto wired by spring boot framework
     */
    @Autowired
    private ModelMapper modelMapper;
    

    /**
     * This method will save {@Code Author} data into database using {@Code AuthorRepository} if 
     * data provided is valid. 
     * Returns {@Code AuthorDTO} after saving data into database. If the first name or last name 
     * is not provided, then there will be an error 
     * 
     * @param authorDto {@Code AuthorDTO} object that will be saved into database
     * @return {@Code AuthorDTO} that is returned from database repository
     * @throws AuthorNullValueException if first name or last name is null or empty
     * @throws AuthorAlreadyExistsException if author with same first and last name already exists 
     */
    @Override
    public AuthorDTO saveAuthor(AuthorDTO authorDto){
        if(authorDto.getFirstName() == null || "" == authorDto.getFirstName()
                        || authorDto.getLastName() == null || "" == authorDto.getLastName()){
            throw new AuthorNullValueException("First Name and Last Name must be provided");
        }
        Author authorDB = authorRepository.findByFirstNameAndLastName(authorDto.getFirstName(), authorDto.getLastName());
        if(authorDB != null){
            throw new AuthorAlreadyExistsException("Author with same First and Last name already exists");
        }
        Author author = new Author(authorDto.getId(),authorDto.getFirstName(),authorDto.getLastName());
        Author savedAuthor = authorRepository.save(author);
        return new AuthorDTO(savedAuthor.getId(),savedAuthor.getFirstName(),savedAuthor.getLastName());
        
        // TODO: findout later on why modelMapper injection not working in test cases
        //Author author = modelMapper.map(authorDto, Author.class);
        //return modelMapper.map(authorRepository.save(author),AuthorDTO.class);
    }

    /**
     * This method will return {@Code List} of {@Code AuthorDTO} from database using {@Code AuthorRepository} if 
     * there is no data then it will return empty list. 
     * 
     * @return {@Code List} of {@Code AuthorDTO} that is returned from database repository 
     */
    @Override
    public List<AuthorDTO> getAllAuthors() {
        return authorRepository.findAll().stream().map(author-> convertEntityToDTO(author)).toList();
    }

    /**
     * This method will return {@Code AuthorDTO} from database using provided id
     * 
     * @param id id of author to be found
     * @return {@Code AuthorDTO} that is returned from database repository
     * @throws AuthorNotFoundException if id provided does not exists
     */
    @Override
    public AuthorDTO getAuthorById(long id) {
        return authorRepository.findById(id).map(author-> convertEntityToDTO(author))
                            .orElseThrow(()-> new AuthorNotFoundException("No Such Author Exists with id "+id));
    }

    /**
     * Update the {@Code Author} data into database using {@Code AuthorRepository} if data provided is valid.
     * 
     * @param authorId id for {@Code Author} that needs to be updated
     * @param authorDto DTO object that will be updated into database
     * @return {@Code AuthorDTO} that is returned from database repository
     * @throws AuthorNotFoundException if id provided does not exists in database
     * @throws DocumentNotFoundException if any {@Code Document} from the list provided does not exists in database
     */
    @Override
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

    /**
     * Delete the {@Code Author} from database using provided id.
     * {@Code Author} cannot be deleted if it is assigned to any document in database.
     * Need to remove reference from those document before deleting the {@Code Author} 
     * 
     * @param id id for {@Code Author} that needs to be deleted
     * @return {@Code true} if {@Code Author} deleted successfully
     * @throws AuthorNotFoundException if id provided does not exists in database
     * @throws DocumentAttachedToAuthorException if any document is assigned to author
     */
    @Override
    public boolean deleteAuthorById(long id){
        Author author = authorRepository.findById(id).orElseThrow(()-> new AuthorNotFoundException("No Such Author Exists with id "+id));
        if(author != null && author.getDocuments() != null && author.getDocuments().size()>0) {
            throw new DocumentAttachedToAuthorException("Author cannot be delete if document is attached");
        }
        if(author != null){
            authorRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Utitlity method to convert {@Code Author} entity to {@Code AuthorDTO} DTO
     * This mehtod will convert all the nested documents {@Code Document} and references {@Code Reference}
     * related to it as well to their respective DTOs
     * 
     * @param authorEntity {@Code Author} that needs to be converted
     * @return {@Code AuthorDTO} after mapping all the properites from {@Code Author}
     */
    @Override
    public AuthorDTO convertEntityToDTO(Author authorEntity) {
        List<DocumentDTO> documentDtos = new ArrayList<DocumentDTO>();
        
        if(authorEntity.getDocuments() != null) {
            documentDtos = authorEntity.getDocuments().stream()
                    .map(document-> {
                        List<ReferenceDTO> referenceDtos = document.getReferences().stream()
                                                .map(reference-> new ReferenceDTO(reference.getId(), reference.getReference())).toList();
                        List<AuthorDTO> authorDtos = document.getAuthors().stream()
                                                .map(author-> new AuthorDTO(author.getId(), author.getFirstName(), author.getLastName())).toList();
                        return new DocumentDTO(document.getId(), document.getTitle(),
                                                    document.getBody(), referenceDtos, authorDtos);
                    }).toList();
        }
        AuthorDTO authorDto = new AuthorDTO(authorEntity.getId(), authorEntity.getFirstName(), authorEntity.getLastName(),documentDtos);
        return authorDto;
    }
}
