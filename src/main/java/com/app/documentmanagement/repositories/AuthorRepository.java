package com.app.documentmanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.documentmanagement.entities.Author;
import java.util.List;


public interface AuthorRepository extends JpaRepository<Author,Long> {
    
    List<Author> findByFirstName(String firstName);
    List<Author> findByLastName(String lastName);
    Author findByFirstNameAndLastName(String firstName, String lastName);
}
