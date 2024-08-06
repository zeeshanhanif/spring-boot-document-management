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
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("documents")
public class DocumentController {
    
    @Autowired
    private DocumentService documentService;

    @GetMapping
    public List<DocumentDTO> getAllDocuments() {
        return documentService.getAllDocuments();
    }

    @GetMapping("/{id}")
    public DocumentDTO getDocumentById(@PathVariable long id) {
        return documentService.getDocumentById(id);
    }

    @PostMapping
    public Document saveDocument(@RequestBody Document document) {
        System.out.println("doc = "+document.toString());
        
        return documentService.saveDocument(document);
    }

    @PutMapping("/{id}")
    public Document updateDocument(@PathVariable long id, @RequestBody Document document) {
        return documentService.updateDocument(id, document);
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
