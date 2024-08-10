package com.app.documentmanagement.controller;

import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.app.documentmanagement.dto.AuthorDTO;
import com.app.documentmanagement.dto.DocumentDTO;
import com.app.documentmanagement.dto.ReferenceDTO;
import com.app.documentmanagement.entities.Author;
import com.app.documentmanagement.entities.Document;
import com.app.documentmanagement.entities.Reference;
import com.app.documentmanagement.exceptions.AuthorNotFoundException;
import com.app.documentmanagement.exceptions.AuthorNullValueException;
import com.app.documentmanagement.exceptions.DocumentAttachedToAuthorException;
import com.app.documentmanagement.services.AuthorService;
import com.app.documentmanagement.services.AuthorServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthorControllerTest {
    
    private final String API_URL = "/api/authors";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthorServiceImpl authorService;

    @Mock
    private ModelMapper modelMapper;

    @Autowired
    protected ObjectMapper objectMapper;

    private List<Author> authors;

    @BeforeEach
    public void setup() {
        authors = populateAuthorsList();
    }

    @AfterEach
    public void tearDown() {
        authors = null;
    }

    @Test
    public void shouldReturnAllAuthors() throws Exception {
        List<AuthorDTO> expectedAuthorDtos = authors.stream()
                        .map(author-> convertEntityToDTO(author)).collect(Collectors.toList());
        given(authorService.getAllAuthors()).willReturn(expectedAuthorDtos);

        MvcResult mvcResult = mockMvc.perform(get(API_URL).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("Zeeshan"))
                .andExpect(jsonPath("$[0].lastName").value("Hanif"))
                .andReturn();

        String actualResponseAsString = mvcResult.getResponse().getContentAsString();
        assertThat(actualResponseAsString).isEqualToIgnoringWhitespace(
            objectMapper.writeValueAsString(expectedAuthorDtos));
    }

    @Test
    public void shouldReturnAuthorWhenValidIdProvided() throws Exception {
        List<AuthorDTO> expectedAuthorDtos = authors.stream()
                        .map(author-> convertEntityToDTO(author)).collect(Collectors.toList());
        given(authorService.getAuthorById(1)).willReturn(expectedAuthorDtos.get(0));

        MvcResult mvcResult = mockMvc.perform(get(API_URL+"/{id}",1).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Zeeshan"))
                .andExpect(jsonPath("$.lastName").value("Hanif"))
                .andReturn();

        String actualResponseAsString = mvcResult.getResponse().getContentAsString();
        assertThat(actualResponseAsString).isEqualToIgnoringWhitespace(
            objectMapper.writeValueAsString(expectedAuthorDtos.get(0)));
    }

    @Test
    public void shouldReturnErrorWhenNonExistentIdProvided() throws Exception {
        long authorIdToFind = 11;
        given(authorService.getAuthorById(authorIdToFind)).willThrow(new AuthorNotFoundException("No Such Author Exists with id "+authorIdToFind));

        mockMvc.perform(get(API_URL+"/{id}",authorIdToFind).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.statusCode").value(404))
                .andExpect(jsonPath("$.message").value("No Such Author Exists with id "+authorIdToFind));
    }

    @Test
    public void shouldDeleteAuthorWhenValidIdProvided() throws Exception {
        long authorId = 1;
        given(authorService.deleteAuthorById(authorId)).willReturn(true);

        mockMvc.perform(delete(API_URL+"/{id}",authorId).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Delete Successfully"))
                .andReturn();
    }

    @Test
    public void shouldFailToDeleteAuthorWhenInvalidIdProvided() throws Exception {
        long authorId = 11;
        given(authorService.deleteAuthorById(authorId)).willThrow(new AuthorNotFoundException("No Such Author Exists with id "+authorId));

        mockMvc.perform(delete(API_URL+"/{id}",authorId).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.statusCode").value(404))
                .andExpect(jsonPath("$.message").value("No Such Author Exists with id "+authorId))
                .andReturn();
    }

    @Test
    public void shouldFailToDeleteAuthorWhenDoucmentIsAttached() throws Exception {
        long authorId = 1;
        given(authorService.deleteAuthorById(authorId)).willThrow(new DocumentAttachedToAuthorException("Author cannot be delete if document is attached"));

        mockMvc.perform(delete(API_URL+"/{id}",authorId).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.message").value("Author cannot be delete if document is attached"))
                .andReturn();
    }

    @Test
    public void shouldSaveAuthorWhenValidDataProvided() throws Exception {
        Author expectedAuthor = authors.get(0);
        AuthorDTO expectedAuthorDto = convertEntityToDTO(expectedAuthor);
        given(authorService.saveAuthor(Mockito.any(AuthorDTO.class))).willReturn(expectedAuthorDto);

        MvcResult mvcResult = mockMvc.perform(post(API_URL).contentType(MediaType.APPLICATION_JSON)
                                            .content(objectMapper.writeValueAsString(expectedAuthorDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Zeeshan"))
                .andExpect(jsonPath("$.lastName").value("Hanif"))
                .andReturn();

        String actualResponseAsString = mvcResult.getResponse().getContentAsString();
        Author actualAutor = objectMapper.readValue(actualResponseAsString, Author.class);
        
        assertThat(actualAutor.getId()).isEqualTo(expectedAuthorDto.getId());
        assertThat(actualAutor.getFirstName()).isEqualTo(expectedAuthorDto.getFirstName());
        assertThat(actualAutor.getLastName()).isEqualTo(expectedAuthorDto.getLastName());       
    }

    @Test
    public void shouldFailSaveAuthorWhenInvalidDataProvided() throws Exception {
        AuthorDTO expectedAuthorDto = new AuthorDTO();
        given(authorService.saveAuthor(Mockito.any(AuthorDTO.class)))
                .willThrow(new AuthorNullValueException("First Name and Last Name must be provided"));

        mockMvc.perform(post(API_URL).contentType(MediaType.APPLICATION_JSON)
                                            .content(objectMapper.writeValueAsString(expectedAuthorDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.errors.firstName").value("First Name is mandatory"))
                .andExpect(jsonPath("$.errors.lastName").value("Last Name is mandatory"))
                .andReturn();   
    }

    /**
     * NOTE: This test case should verify that update of author is working as expected.
     * Testcase returns the status ok, but response body is empty which should not be the case
     * and it should contain object
     * 
     * @throws Exception
     */
    @Disabled
    @Test
    public void shouldUpdateAuthorWhenValidDataProvided() throws Exception {
        Author expectedAuthor = authors.get(0);
        AuthorDTO expectedAuthorDto = convertEntityToDTO(expectedAuthor);
        long authorId = 1;
        given(authorService.getAuthorById(authorId)).willReturn(expectedAuthorDto);

        expectedAuthorDto.setFirstName("Shan");

        given(authorService.updateAuthor(authorId,expectedAuthorDto)).willReturn(expectedAuthorDto);
        
        MvcResult mvcResult = mockMvc.perform(put(API_URL+"/{id}",authorId).contentType(MediaType.APPLICATION_JSON)
                                            .content(objectMapper.writeValueAsString(expectedAuthorDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Shan"))
                .andExpect(jsonPath("$.lastName").value("Hanif"))
                .andReturn();
        
        String actualResponseAsString = mvcResult.getResponse().getContentAsString();    
    }
 
    private List<Author> populateAuthorsList() {
        return Arrays.asList(
            new Author(1, "Zeeshan","Hanif"),
            new Author(2, "Zain","Hanif"),
            new Author(3, "Inam","ul Haq"),
            new Author(4, "Rehan","Uddin"),
            new Author(5, "Taha","Ahmed")
        );
    }

    private Document populateSingleDocument() {
        ArrayList<Reference> references = new ArrayList<>();
        references.add(new Reference("Nielsen, M. A., & Chuang, I. L. (2010). Quantum Computation and Quantum Information: 10th Anniversary Edition. Cambridge University Press"));
        references.add(new Reference("Preskill, J. (2018). Quantum Computing in the NISQ era and beyond. Quantum, 2, 79."));
        Document document = new Document("Exploring Quantum Computing","Quantum computing is poised to revolutionize fields ranging from cryptography to complex system simulations. This document provides an overview of quantum computing principles, including superposition and entanglement. It examines current developments and potential applications of quantum technology. The document also discusses challenges in the field, such as scalability and error correction.",
        references);
        return document;
    }

    public AuthorDTO convertEntityToDTO(Author authorEntity) {
        List<DocumentDTO> documentDtos = new ArrayList<DocumentDTO>();
        if(authorEntity.getDocuments() != null) {
            documentDtos = authorEntity.getDocuments().stream()
                    .map(document-> {
                        List<ReferenceDTO> referenceDtos = document.getReferences().stream()
                                                .map(reference-> new ReferenceDTO(reference.getId(), reference.getReference())).collect(Collectors.toList());
                        List<AuthorDTO> authorDtos = document.getAuthors().stream()
                                                .map(author-> new AuthorDTO(author.getId(), author.getFirstName(), author.getLastName())).collect(Collectors.toList());
                        return new DocumentDTO(document.getId(), document.getTitle(),
                                                    document.getBody(), referenceDtos, authorDtos);
                    }).collect(Collectors.toList());
        }
        AuthorDTO authorDto = new AuthorDTO(authorEntity.getId(), authorEntity.getFirstName(), authorEntity.getLastName(),documentDtos);
        return authorDto;
    }
}
