package com.app.documentmanagement.entities;

import java.util.List;

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

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
//@ToString
@Entity
@Table(name = "documents")
public class Document {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false)
    private String title;
        
    @Size(max = 1000)
    @Column(length = 1000, nullable = false)
    private String body;
    
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "document_id")
    private List<Reference> references;

    @ManyToMany
    @JoinTable(
        name = "document_author",
        joinColumns = {@JoinColumn(name="documentId",referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name="authorId", referencedColumnName = "id")}
    )
    private List<Author> authors;

    public Document(String title, String body, List<Reference> references) {
        this.title = title;
        this.body = body;
        this.references = references;
    }

}
