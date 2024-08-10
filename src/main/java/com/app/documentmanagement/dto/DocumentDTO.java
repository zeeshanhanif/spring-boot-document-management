package com.app.documentmanagement.dto;

import java.util.List;

import com.app.documentmanagement.entities.Author;
import com.app.documentmanagement.entities.Reference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Data Transfer Object for {@Code Document} to transfer data from front-end to back-end and vice versa. 
 * 
 * @author  Zeeshan Hanif
 * @see     Author
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@JsonInclude(Include.NON_NULL)
public class DocumentDTO {
    
    /**
     * identification of object in database
     */
    private long id;

    /**
     * Document's title and it should not be empty
     */
    @NotBlank(message = "Title is mandatory")
    private String title;

    /**
     * Document's body and it should not be empty
     */
    @NotBlank(message = "Doucment body is mandatory")
    private String body;

    /**
     * Document's references and this list should not be empty
     * {@Code ReferenceDTO} will be also validated at the time of input
     * 
     * @see Reference
     * @see ReferenceDTO
     */
    @Valid
    @NotEmpty(message = "References must not be empty")
    private List<ReferenceDTO> references;

    /**
     * Document's authors and this list should not be empty
     * 
     * @see Author
     * @see AuthorDTO
     */
    @NotEmpty(message = "Authors must not be empty")
    private List<AuthorDTO> authors;
    
    /**
     * Constructs {@Code DocumentDTO} with specified initial field values
     * 
     * @param id unique id for document
     * @param title title of document
     * @param body body of document
     */
    public DocumentDTO(long id, String title, String body) {
        this.id = id;
        this.title = title;
        this.body = body;
    }

    /**
     * Constructs {@Code DocumentDTO} with specified initial field values including references
     * 
     * @param id unique id for document
     * @param title title of document
     * @param body body of document
     * @param references list of references of document
     */
    public DocumentDTO(long id, String title, String body, List<ReferenceDTO> references) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.references = references;
    }

}
