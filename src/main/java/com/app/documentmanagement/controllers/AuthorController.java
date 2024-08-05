package com.app.documentmanagement.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.app.documentmanagement.entities.Author;
import com.app.documentmanagement.repositories.AuthorRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("authors")
public class AuthorController {
    
    @Autowired
    private AuthorRepository authorRepository;

    @GetMapping("getData")
    public List<Author> getData() {
        return authorRepository.findAll();
    }

    @PostMapping("add")
    public Author addAuthor(@RequestBody Author author) {
        
        Author savedAuthor = authorRepository.save(author);
        
        return savedAuthor;
    }
    
}
