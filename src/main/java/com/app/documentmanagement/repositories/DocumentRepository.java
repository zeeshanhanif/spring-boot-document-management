package com.app.documentmanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.documentmanagement.entities.Document;

/**
 * {@DocumentRepository} class is used crud operation on database, in inherits all the methods from
 * parent interface
 * 
 * @author Zeeshan Hanif
 */
public interface DocumentRepository extends JpaRepository<Document, Long> {
    
}
