package com.app.documentmanagement.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Database Entity for {@Code Reference} that represent table in database
 * 
 * @author  Zeeshan Hanif
 * @see     Author
 * @see     Reference
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "references")
public class Reference {
    
    /**
     * identification of object in database
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    /**
     * Reference's value and it should not be empty
     */
    @Column(nullable = false)
    private String reference;

    /**
     * Constructs {@Code Reference} with specified initial field values
     * 
     * @param reference title of document
     */
    public Reference(String reference) {
        this.reference = reference;
    }

}
