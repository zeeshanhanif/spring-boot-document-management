package com.app.documentmanagement.dto;

import java.util.List;

import com.app.documentmanagement.entities.Author;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Data Transfer Object for {@Code Author} to transfer data from front-end to back-end and vice versa. 
 * 
 * @author  Zeeshan Hanif
 * @see     Author
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonInclude(Include.NON_NULL)
public class AuthorDTO {
    
    /**
     * identification of object in database
     */
    private long id;

    /**
     * Author's first name and it should not be empty
     */
    @NotBlank(message = "First Name is mandatory")
    private String firstName;

    /**
     * Author's last name and it should not be empty
     */
    @NotBlank(message = "Last Name is mandatory")
    private String lastName;

    /**
     * List Documents this author is part of. Documents list initially can be empty
     * 
     * @see {@Code DocumentDTO}
     * @see {@Code Document}
     */
    private List<DocumentDTO> documents;

    /**
     * Constructs {@Code AuthorDTO} with specified initial field values
     * 
     * @param id unique id for author
     * @param firstName first name of author
     * @param lastName last name of author
     */
    public AuthorDTO(long id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

}
