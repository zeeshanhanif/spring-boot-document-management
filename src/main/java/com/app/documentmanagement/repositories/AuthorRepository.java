package com.app.documentmanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.documentmanagement.entities.Author;

public interface AuthorRepository extends JpaRepository<Author,Long> {
    
}
