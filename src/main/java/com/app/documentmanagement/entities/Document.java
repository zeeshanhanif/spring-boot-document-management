package com.app.documentmanagement.entities;

import java.util.List;

import com.app.documentmanagement.dto.AuthorDTO;
import com.app.documentmanagement.dto.ReferenceDTO;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Database Entity for {@Code Document} that represent table in database
 * 
 * @author  Zeeshan Hanif
 * @see     Author
 * @see     Reference
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
//@ToString
@Entity
@Table(name = "documents")
public class Document {
    
    /**
     * identification of object in database
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    /**
     * Document's title and it should not be empty
     */
    @Column(nullable = false)
    private String title;
       
    /**
     * Document's body, it should not be empty and can not exceed 1000 character
     */
    @Size(max = 1000)
    @Column(length = 1000, nullable = false)
    private String body;
    
    /**
     * List of Document's references and this list should not be empty
     * {@Code Reference} will be also validated at the time of input
     * 
     * @see Reference
     */
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "document_id")
    private List<Reference> references;

    /**
     * List of Document's authors and this list should not be empty
     * This field works as many to many relationship with {@Code Document}
     * 
     * @see Author
     */
    @ManyToMany
    @JoinTable(
        name = "document_author",
        joinColumns = {@JoinColumn(name="documentId",referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name="authorId", referencedColumnName = "id")}
    )
    private List<Author> authors;

    /**
     * Constructs {@Code Document} with specified initial field values
     * 
     * @param title title of document
     * @param body body of document
     * @param references list of references of document
     */
    public Document(String title, String body, List<Reference> references) {
        this.title = title;
        this.body = body;
        this.references = references;
    }

}
