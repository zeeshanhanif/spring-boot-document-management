package com.app.documentmanagement.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.documentmanagement.dto.DocumentDTO;
import com.app.documentmanagement.entities.Document;
import com.app.documentmanagement.services.DocumentService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("api/documents")
public class DocumentController {
    
    @Autowired
    private DocumentService documentService;

    @GetMapping
    public ResponseEntity<List<DocumentDTO>> getAllDocuments() {
        return ResponseEntity.status(HttpStatus.OK).body(documentService.getAllDocuments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentDTO> getDocumentById(@PathVariable long id) {
        return ResponseEntity.status(HttpStatus.OK).body(documentService.getDocumentById(id));
    }

    @PostMapping
    public ResponseEntity<DocumentDTO> saveDocument(@Valid @RequestBody DocumentDTO documentDto) {
        return ResponseEntity.status(HttpStatus.OK).body(documentService.saveDocument(documentDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DocumentDTO> updateDocument(@PathVariable long id, @RequestBody DocumentDTO documentDto) {
        return ResponseEntity.status(HttpStatus.OK).body(documentService.updateDocument(id, documentDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDocumentById(@PathVariable long id){
        boolean result = documentService.deleteDocumentById(id);
        if(result) {
            return ResponseEntity.status(HttpStatus.OK).body("Delete Successfully");
        }
        else {
            return ResponseEntity.status(HttpStatus.OK).body("Unable to Delete");
        }
    }
}
