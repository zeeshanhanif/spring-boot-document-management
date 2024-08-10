package com.app.documentmanagement.dto;

import com.app.documentmanagement.entities.Author;
import com.app.documentmanagement.entities.Reference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Data Transfer Object for {@Code Reference} to transfer data from front-end to back-end and vice versa. 
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
public class ReferenceDTO {
    
    /**
     * identification of object in database
     */
    private long id;

    /**
     * Reference's value and it should not be empty
     */
    @NotEmpty(message = "Reference is mandatory")
    private String reference;
    
    /**
     * Constructs {@Code ReferenceDTO} with specified initial field values
     * 
     * @param reference title of document
     */
    public ReferenceDTO(String reference) {
        this.reference = reference;
    }

    
}
