package com.app.documentmanagement.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.app.documentmanagement.dto.AuthorDTO;
import com.app.documentmanagement.services.AuthorService;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import jakarta.validation.Valid;


@RestController
@RequestMapping("api/authors")
public class AuthorController {

    private static final Logger log = LoggerFactory.getLogger(AuthorController.class);

    @Autowired
    private AuthorService authorService;

    @GetMapping
    public ResponseEntity<List<AuthorDTO>> getAllAuthors() {
        return ResponseEntity.status(HttpStatus.OK).body(authorService.getAllAuthors());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorDTO> getAuthorById(@PathVariable long id) {
        return ResponseEntity.status(HttpStatus.OK).body(authorService.getAuthorById(id));
    }

    @PostMapping
    public ResponseEntity<AuthorDTO> saveAuthor(@Valid @RequestBody AuthorDTO authorDto) {
        return ResponseEntity.status(HttpStatus.OK).body(authorService.saveAuthor(authorDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuthorDTO> updateAuthor(@PathVariable long id, @RequestBody AuthorDTO authorDto) {
        return ResponseEntity.status(HttpStatus.OK).body(authorService.updateAuthor(id, authorDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAuthorById(@PathVariable long id){
        boolean result = authorService.deleteAuthorById(id);
        
        if(result) {
            return ResponseEntity.status(HttpStatus.OK).body("Delete Successfully");
        }
        else {
            return ResponseEntity.status(HttpStatus.OK).body("Unable to Delete");
        }
    }
    
}
