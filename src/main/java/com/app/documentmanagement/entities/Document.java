package com.app.documentmanagement.entities;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "documents")
public class Document {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String title;
    private String body;
    private List<String> references;

    @ManyToMany
    @JoinTable(
        name = "document_author",
        joinColumns = {@JoinColumn(name="documentId",referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name="authorId", referencedColumnName = "id")}
    )
    private List<Author> authors;
    

}
