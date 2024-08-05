package com.app.documentmanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.documentmanagement.entities.Document;

public interface DocumentRepository extends JpaRepository<Document, Long> {
    
}
