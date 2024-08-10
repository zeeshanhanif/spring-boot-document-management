package com.app.documentmanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.documentmanagement.entities.Reference;

/**
 * {@ReferenceRepository} class is used crud operation on database, in inherits all the methods from
 * parent interface
 * 
 * @author Zeeshan Hanif
 */
public interface ReferenceRepository extends JpaRepository<Reference, Long> {
    
}
