package com.app.documentmanagement.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.documentmanagement.dto.AuthorDTO;
import com.app.documentmanagement.dto.DocumentDTO;
import com.app.documentmanagement.services.DocumentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * {@Code DocumentController} is exposing api endpoints for CRUD operation and management of Document
 * 
 * @author Zeeshan Hanif
 */
@Tag(name = "Document", description = "Document Management APIs")
@RestController
@RequestMapping("api/documents")
public class DocumentController {
    
    /**
     * {@Code documentService} to interact with document
     * It will be auto wired by spring boot framework
     */
    @Autowired
    private DocumentService documentService;

    /**
     * This method is exposing GET 'api/authors' endpoint to get all the documents {@Code Document} 
     * 
     * @return {@Code List} of {@Code DocumentDTO} that is returned from {@Code DocumentService} 
     */
    @Operation(summary = "Get all the Doucments", description = "Get all the Doucments object. The response is List of Doucment object with id, title, body, references and authors",
                        tags = { "Get" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Found all the documents", content = { @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = DocumentDTO.class))) }),
    })
    @GetMapping
    public ResponseEntity<List<DocumentDTO>> getAllDocuments() {
        return ResponseEntity.status(HttpStatus.OK).body(documentService.getAllDocuments());
    }

    /**
     * This method is exposing GET 'api/authors/{id}' endpoint to get {@Code Document} specified by id 
     * 
     * @return {@Code DocumentDTO} that is returned from {@Code DocumentService} 
     */
    @Operation(summary = "Get a document by its id", description = "Get a Doucment object by specifying its id. The response is a Document object with id, title, body, references and authors",
                        tags = { "Get" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Found document with specified id", content = { @Content(schema = @Schema(implementation = DocumentDTO.class), mediaType = "application/json") }),
        @ApiResponse(responseCode = "404", description = "Document not found", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/{id}")
    public ResponseEntity<DocumentDTO> getDocumentById(@PathVariable long id) {
        return ResponseEntity.status(HttpStatus.OK).body(documentService.getDocumentById(id));
    }

    /**
     * This method is exposing POST 'api/authors' endpoint to save {@Code Document} in system 
     * 
     * @return {@Code DocumentDTO} that is returned from {@Code DocumentService} after saving into database
     */
    @Operation(summary = "Add Document into system", description = "Add a Document object into system. The response is a Document object with id, title, body, references and authors",
                        tags = { "Post" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Document add successfully", content = { @Content(schema = @Schema(implementation = DocumentDTO.class), mediaType = "application/json") }),
        @ApiResponse(responseCode = "400", description = "There can be three different erors:<br>1) Title and body must be provided<br>OR<br>2) References must be provided<br>OR<br>3) Authors must be provided", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "404", description = "Author that we are assigning to document not fund", content = @Content(mediaType = "application/json"))
    })
    @PostMapping
    public ResponseEntity<DocumentDTO> saveDocument(@Valid @RequestBody DocumentDTO documentDto) {
        return ResponseEntity.status(HttpStatus.OK).body(documentService.saveDocument(documentDto));
    }

    /**
     * This method is exposing PUT 'api/authors/{id}' endpoint to update {@Code Document} in system 
     * 
     * @return {@Code DocumentDTO} that is returned from {@Code DocumentService} after updated into database
     */
    @Operation(summary = "Update document by id", description = "Update an document object into system by specifying its id. The response is a Document object with  id and updated title, body, references and authors",
                        tags = { "Put" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Author updated successfully", content = { @Content(schema = @Schema(implementation = AuthorDTO.class), mediaType = "application/json") }),
        @ApiResponse(responseCode = "404", description = "There can be two different erors:<br>1) Document not found with specified id<br>OR<br>2) Author not found, when try to update author of document but that author does not exists in system", content = @Content(mediaType = "application/json")),
    })
    @PutMapping("/{id}")
    public ResponseEntity<DocumentDTO> updateDocument(@PathVariable long id, @RequestBody DocumentDTO documentDto) {
        return ResponseEntity.status(HttpStatus.OK).body(documentService.updateDocument(id, documentDto));
    }

    /**
     * This method is exposing DELETE 'api/authors/{id}' endpoint to delete {@Code Document} specified by id 
     * 
     * @return {@Code String} message that show delete was successfull or not
     */
    @Operation(summary = "Delete document by id", description = "Delete Document from the system by specifying its id. The response is a message stating that delete successful or not",
                        tags = { "Delete" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Document delete successfully", content = { @Content(schema = @Schema(implementation = AuthorDTO.class), mediaType = "application/json") }),
        @ApiResponse(responseCode = "404", description = "Document not found with specified id", content = @Content(mediaType = "application/json"))
    })
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
