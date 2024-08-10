package com.app.documentmanagement.services;

import java.util.List;

import com.app.documentmanagement.dto.AuthorDTO;
import com.app.documentmanagement.entities.Author;


/**
 * This {@Code AuthorService} interface provides methods for database operations
 * 
 * @author Zeeshan Hanif
 * @see AuthorRepository
 * @see AuthorServiceImpl 
 */
public interface AuthorService {

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
    AuthorDTO saveAuthor(AuthorDTO authorDto);

    /**
     * This method will return {@Code List} of {@Code AuthorDTO} from database using {@Code AuthorRepository} if 
     * there is no data then it will return empty list. 
     * 
     * @return {@Code List} of {@Code AuthorDTO} that is returned from database repository 
     */
    List<AuthorDTO> getAllAuthors();

    /**
     * This method will return {@Code AuthorDTO} from database using provided id
     * 
     * @param id id of author to be found
     * @return {@Code AuthorDTO} that is returned from database repository
     * @throws AuthorNotFoundException if id provided does not exists
     */
    AuthorDTO getAuthorById(long id);

    /**
     * Update the {@Code Author} data into database using {@Code AuthorRepository} if data provided is valid.
     * 
     * @param authorId id for {@Code Author} that needs to be updated
     * @param authorDto DTO object that will be updated into database
     * @return {@Code AuthorDTO} that is returned from database repository
     * @throws AuthorNotFoundException if id provided does not exists in database
     * @throws DocumentNotFoundException if any {@Code Document} from the list provided does not exists in database
     */
    AuthorDTO updateAuthor(long authorId, AuthorDTO authorDto);

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
    boolean deleteAuthorById(long id);

    /**
     * Utitlity method to convert {@Code Author} entity to {@Code AuthorDTO} DTO
     * This mehtod will convert all the nested documents {@Code Document} and references {@Code Reference}
     * related to it as well to their respective DTOs
     * 
     * @param authorEntity {@Code Author} that needs to be converted
     * @return {@Code AuthorDTO} after mapping all the properites from {@Code Author}
     */
    AuthorDTO convertEntityToDTO(Author authorEntity);
}
