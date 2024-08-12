package com.app.documentmanagement.entities;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Database Entity for {@Code Author} that represent table in database
 * 
 * @author  Zeeshan Hanif
 * @see     Document
 * @see     Reference
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "authors")
public class Author {

    /**
     * identification of object in database
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    /**
     * Author's first name and it should not be empty
     */
    @Column(name = "first_name", nullable = false)
    private String firstName;
    
    /**
     * Author's last name and it should not be empty
     */
    @Column(name = "last_name", nullable = false)
    private String lastName;

    /**
     * List Documents this author is part of. Documents list initially can be empty
     * 
     * @see {@Code Document}
     */
    @ManyToMany(mappedBy = "authors", fetch = FetchType.EAGER)
    private List<Document> documents;
    
    /**
     * Constructs {@Code Author} with specified initial field values
     * 
     * @param firstName first name of author
     * @param lastName last name of author
     */
    public Author(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    /**
     * Constructs {@Code Author} with specified initial field values
     * 
     * @param id unique id for author
     * @param firstName first name of author
     * @param lastName last name of author
     */
    public Author(long id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }
    
}
