package com.app.documentmanagement.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.app.documentmanagement.dto.AuthorDTO;
import com.app.documentmanagement.services.AuthorService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

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

@Tag(name = "Author", description = "Author Management APIs")
@RestController
@RequestMapping("api/authors")
public class AuthorController {

    private static final Logger log = LoggerFactory.getLogger(AuthorController.class);

    @Autowired
    private AuthorService authorService;

    @Operation(summary = "Get all the Authors", description = "Get all the Authors object. The response is List of Authors object with id, first name, last name and doucments",
                        tags = { "Get" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Found all the authors", content = { @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = AuthorDTO.class))) }),
    })
    @GetMapping
    public ResponseEntity<List<AuthorDTO>> getAllAuthors() {
        return ResponseEntity.status(HttpStatus.OK).body(authorService.getAllAuthors());
    }

    @Operation(summary = "Get a author by its id", description = "Get a Author object by specifying its id. The response is a Author object with id, first name, last name and doucments",
                        tags = { "Get" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Found author with specified id", content = { @Content(schema = @Schema(implementation = AuthorDTO.class), mediaType = "application/json") }),
        @ApiResponse(responseCode = "404", description = "Author not found", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/{id}")
    public ResponseEntity<AuthorDTO> getAuthorById(@PathVariable long id) {
        return ResponseEntity.status(HttpStatus.OK).body(authorService.getAuthorById(id));
    }

    @Operation(summary = "Add Author into system", description = "Add a Author object into system. The response is a Author object with id, first name, last name and doucments",
                        tags = { "Post" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Author add successfully", content = { @Content(schema = @Schema(implementation = AuthorDTO.class), mediaType = "application/json") }),
        @ApiResponse(responseCode = "400", description = "Authors First name or Last name not provided", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "409", description = "Author already exists with similar first and last name", content = @Content(mediaType = "application/json"))
    })
    @PostMapping
    public ResponseEntity<AuthorDTO> saveAuthor(@Valid @RequestBody AuthorDTO authorDto) {
        return ResponseEntity.status(HttpStatus.OK).body(authorService.saveAuthor(authorDto));
    }

    @Operation(summary = "Update author by id", description = "Update an Author object into system by specifying its id. The response is a Author object with  id and updated first name, last name and doucments",
                        tags = { "Put" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Author updated successfully", content = { @Content(schema = @Schema(implementation = AuthorDTO.class), mediaType = "application/json") }),
        @ApiResponse(responseCode = "404", description = "There can be two different erors:<br>1) Author not found with specified id<br>OR<br>2) Document not found, when try to update document of author but that document does not exists in system", content = @Content(mediaType = "application/json")),
    })
    @PutMapping("/{id}")
    public ResponseEntity<AuthorDTO> updateAuthor(@PathVariable long id, @RequestBody AuthorDTO authorDto) {
        return ResponseEntity.status(HttpStatus.OK).body(authorService.updateAuthor(id, authorDto));
    }

    @Operation(summary = "Delete author by id", description = "Delete Author from the system by specifying its id. The response is a message stating that delete successful or not",
                        tags = { "Delete" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Author delete successfully", content = { @Content(schema = @Schema(implementation = AuthorDTO.class), mediaType = "application/json") }),
        @ApiResponse(responseCode = "404", description = "Author not found with specified id", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "400", description = "Author cannot be delete if document is attached to author", content = @Content(mediaType = "application/json"))
    })
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
