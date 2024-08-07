package com.app.documentmanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.documentmanagement.entities.Reference;

public interface ReferenceRepository extends JpaRepository<Reference, Long> {
    
}
