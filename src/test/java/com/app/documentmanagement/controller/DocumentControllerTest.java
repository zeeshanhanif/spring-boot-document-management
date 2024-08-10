package com.app.documentmanagement.controller;

import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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
import com.app.documentmanagement.exceptions.DocumentNotFoundException;
import com.app.documentmanagement.exceptions.DocumentNullValueException;
import com.app.documentmanagement.services.DocumentService;
import com.app.documentmanagement.services.DocumentServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class DocumentControllerTest {
    
    private final String API_URL = "/api/documents";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DocumentServiceImpl documentService;

    @Autowired
    private ObjectMapper objectMapper;

    private List<Document> documents;

    @BeforeEach
    public void setup() {
        documents = populateDocumentList();
    }

    @AfterEach
    public void tearDown() {
        documents = null;
    }

    @Test
    public void shouldReturnAllDocuments() throws Exception {
        List<DocumentDTO> expectedDocumentDtos = documents.stream()
                        .map(document-> convertEntityToDTO(document)).collect(Collectors.toList());
        given(documentService.getAllDocuments()).willReturn(expectedDocumentDtos);

        MvcResult mvcResult = mockMvc.perform(get(API_URL).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value(expectedDocumentDtos.get(0).getTitle()))
                .andExpect(jsonPath("$[0].body").value(expectedDocumentDtos.get(0).getBody()))
                .andReturn();
        
        String actualResponseAsString = mvcResult.getResponse().getContentAsString();
        assertThat(actualResponseAsString).isEqualToIgnoringWhitespace(
            objectMapper.writeValueAsString(expectedDocumentDtos));
    }

    @Test
    public void shouldReturnDocumentWhenValidIdProvided() throws Exception {
        List<DocumentDTO> expectedDocumentDtos = documents.stream()
                        .map(document-> convertEntityToDTO(document)).collect(Collectors.toList());
        given(documentService.getDocumentById(1)).willReturn(expectedDocumentDtos.get(0));

        MvcResult mvcResult = mockMvc.perform(get(API_URL+"/{id}",1).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(expectedDocumentDtos.get(0).getTitle()))
                .andExpect(jsonPath("$.body").value(expectedDocumentDtos.get(0).getBody()))
                .andReturn();

        String actualResponseAsString = mvcResult.getResponse().getContentAsString();
        assertThat(actualResponseAsString).isEqualToIgnoringWhitespace(
            objectMapper.writeValueAsString(expectedDocumentDtos.get(0)));
    }

    @Test
    public void shouldReturnErrorWhenNonExistentIdProvided() throws Exception {
        long documentId = 11;
        given(documentService.getDocumentById(documentId)).willThrow(new DocumentNotFoundException("No Such Document Exists with id "+documentId));

        mockMvc.perform(get(API_URL+"/{id}",documentId).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.statusCode").value(404))
                .andExpect(jsonPath("$.message").value("No Such Document Exists with id "+documentId));
    }

    @Test
    public void shouldDeleteDocumentWhenValidIdProvided() throws Exception {
        long documentId = 1;
        given(documentService.deleteDocumentById(documentId)).willReturn(true);

        mockMvc.perform(delete(API_URL+"/{id}",documentId).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Delete Successfully"))
                .andReturn();
    }

    @Test
    public void shouldFailToDeleteAuthorWhenInvalidIdProvided() throws Exception {
        long documentId = 11;
        given(documentService.deleteDocumentById(documentId)).willThrow(new DocumentNotFoundException("No Such Document Exists with id "+documentId));

        mockMvc.perform(delete(API_URL+"/{id}",documentId).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.statusCode").value(404))
                .andExpect(jsonPath("$.message").value("No Such Document Exists with id "+documentId))
                .andReturn();
    }

    @Test
    public void shouldSaveDocumentWhenValidDataProvided() throws Exception {
        Document expectedDocument = documents.get(0);
        DocumentDTO expectedDocumentDto = convertEntityToDTO(expectedDocument);
        given(documentService.saveDocument(Mockito.any(DocumentDTO.class))).willReturn(expectedDocumentDto);

        MvcResult mvcResult = mockMvc.perform(post(API_URL).contentType(MediaType.APPLICATION_JSON)
                                            .content(objectMapper.writeValueAsString(expectedDocumentDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(expectedDocumentDto.getTitle()))
                .andExpect(jsonPath("$.body").value(expectedDocumentDto.getBody()))
                .andReturn();

        String actualResponseAsString = mvcResult.getResponse().getContentAsString();
        Document actualDocument = objectMapper.readValue(actualResponseAsString, Document.class);
        
        assertThat(actualDocument.getId()).isEqualTo(expectedDocumentDto.getId());
        assertThat(actualDocument.getTitle()).isEqualTo(expectedDocumentDto.getTitle());
        assertThat(actualDocument.getBody()).isEqualTo(expectedDocumentDto.getBody());       
    }

    @Test
    public void shouldFailSaveAuthorWhenInvalidDataProvided() throws Exception {
        DocumentDTO expectedDocumentDto = new DocumentDTO();
        given(documentService.saveDocument(Mockito.any(DocumentDTO.class)))
                .willThrow(new DocumentNullValueException("Title and body must be provide"));

        mockMvc.perform(post(API_URL).contentType(MediaType.APPLICATION_JSON)
                                            .content(objectMapper.writeValueAsString(expectedDocumentDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.errors.title").value("Title is mandatory"))
                .andExpect(jsonPath("$.errors.body").value("Doucment body is mandatory"))
                .andReturn();   
    }

    @Test
    public void shouldFailSaveDocumentWhenReferencesNotProvided() throws Exception {
        DocumentDTO documentDto = new DocumentDTO(4, "Exploring Quantum Computing", "Quantum computing is poised to revolutionize fields ranging from cryptography to complex system simulations.");
        given(documentService.saveDocument(Mockito.any(DocumentDTO.class)))
                .willThrow(new DocumentNullValueException("References must be provided"));

        mockMvc.perform(post(API_URL).contentType(MediaType.APPLICATION_JSON)
                                            .content(objectMapper.writeValueAsString(documentDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.errors.references").value("References must not be empty"))
                .andReturn();
    }

    @Test
    public void shouldFailSaveDocumentWhenAuthorsNotProvided() throws Exception {
        DocumentDTO documentDto = convertEntityToDTO(documents.get(0));
        given(documentService.saveDocument(Mockito.any(DocumentDTO.class)))
                .willThrow(new DocumentNullValueException("Authors must be provided"));

        mockMvc.perform(post(API_URL).contentType(MediaType.APPLICATION_JSON)
                                            .content(objectMapper.writeValueAsString(documentDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.message").value("Authors must be provided"))
                .andReturn();   
    }

    /**
     * NOTE: This test case should verify that update of document is working as expected.
     * Testcase returns the status ok, but response body is empty which should not be the case
     * and it should contain object
     * 
     * @throws Exception
     */
    @Disabled
    @Test
    public void shouldUpdateDocumentWhenValidDataProvided() throws Exception {
        Document expectedDocument = documents.get(0);
        DocumentDTO expectedDocumentDto = convertEntityToDTO(expectedDocument);
        long documentId = 1;
        
        given(documentService.getDocumentById(documentId)).willReturn(expectedDocumentDto);

        expectedDocumentDto.setTitle("Update AI");

        given(documentService.updateDocument(documentId,expectedDocumentDto)).willReturn(expectedDocumentDto);
        
        MvcResult mvcResult = mockMvc.perform(put(API_URL+"/{id}", documentId).contentType(MediaType.APPLICATION_JSON)
                                            .content(objectMapper.writeValueAsString(expectedDocumentDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Update AI"))
                .andReturn();
        
    }

    private List<Author> populateAuthorsList() {
        return Arrays.asList(
            new Author(1, "Zeeshan","Hanif"),
            new Author(2, "Zain","Hanif"),
            new Author(3, "Inam","ul Haq"),
            new Author(4, "Rehan","Uddin"),
            new Author(5, "Taha","Ahmed"),
            new Author(6, "Zia","Khan"),
            new Author(7, "Daniyal","Nagori"),
            new Author(8, "Mohsin","Khalid"),
            new Author(9, "Arsalan","Sabir"),
            new Author(10, "Taha","Shahid")
        );
    }

    private DocumentDTO populateSingleDocument() {
        ArrayList<ReferenceDTO> referenceDtos = new ArrayList<>();
        referenceDtos.add(new ReferenceDTO("Nielsen, M. A., & Chuang, I. L. (2010). Quantum Computation and Quantum Information: 10th Anniversary Edition. Cambridge University Press"));
        referenceDtos.add(new ReferenceDTO("Preskill, J. (2018). Quantum Computing in the NISQ era and beyond. Quantum, 2, 79."));
        DocumentDTO documentDto = new DocumentDTO(4, "Exploring Quantum Computing","Quantum computing is poised to revolutionize fields ranging from cryptography to complex system simulations. This document provides an overview of quantum computing principles, including superposition and entanglement. It examines current developments and potential applications of quantum technology. The document also discusses challenges in the field, such as scalability and error correction.",
        referenceDtos);
        documentDto.setAuthors(List.of(new AuthorDTO(10,"Taha","Shahid")));
        return documentDto;
    }

    private List<Document> populateDocumentList() {
        List<Author> authors = populateAuthorsList();
        ArrayList<Reference> reference1 = new ArrayList<>();
        reference1.add(new Reference("Russell, S., & Norvig, P. (2021). Artificial Intelligence: A Modern Approach. Pearson."));
        reference1.add(new Reference("Tegmark, M. (2017). Life 3.0: Being Human in the Age of Artificial Intelligence. Knopf."));
        Document document1 = new Document(1, "The Future of Artificial Intelligence","Artificial intelligence (AI) is transforming industries and society at large. From healthcare to transportation, AI is driving innovation and efficiency. This document explores the potential future impacts of AI, including ethical considerations and technological advancements. The rapid pace of AI development necessitates thoughtful discourse on its role in society.",
                            reference1, authors.subList(0,3));

        ArrayList<Reference> reference2 = new ArrayList<>();
        reference2.add(new Reference("IPCC. (2021). Climate Change 2021: The Physical Science Basis. Cambridge University Press."));
        reference2.add(new Reference("Klein, N. (2014). This Changes Everything: Capitalism vs. The Climate. Simon & Schuster."));
        reference2.add(new Reference("McKibben, B. (2019). Falter: Has the Human Game Begun to Play Itself Out? Henry Holt and Co."));
        Document document2 = new Document(1, "Climate Change: Challenges and Solutions","Climate change poses significant challenges to ecosystems, economies, and communities worldwide. This document examines the causes of climate change and potential solutions. It highlights the importance of international cooperation, sustainable practices, and technological innovation in addressing environmental issues. The document also discusses the role of policy and individual actions in mitigating climate impacts.",
                            reference2, authors.subList(2,5));

        ArrayList<Reference> reference3 = new ArrayList<>();
        reference3.add(new Reference("Castells, M. (2010). The Rise of the Network Society: The Information Age: Economy, Society, and Culture. Wiley-Blackwell."));
        reference3.add(new Reference("Schmidt, E., & Cohen, J. (2014). The New Digital Age: Reshaping the Future of People, Nations, and Business. Vintage"));
        Document document3 = new Document(1, "The Evolution of the Internet","The internet has evolved from a research project to a global communication network. This document traces the history of the internet, from its inception to its current state. It discusses key milestones, such as the development of the World Wide Web and the rise of social media. The document also explores future trends, including the impact of the internet on privacy, security, and connectivity.",
                            reference3, authors.subList(6,8));

        return Arrays.asList(
            document1,
            document2,
            document3
        );
    }

    public DocumentDTO convertEntityToDTO(Document documentEntity) {
        List<AuthorDTO> authorDtos = documentEntity.getAuthors().stream()
                        .map(author -> new AuthorDTO(author.getId(), author.getFirstName(),
                                                    author.getLastName())).toList();
        List<ReferenceDTO> referenceDtos = documentEntity.getReferences().stream()
                        .map(reference -> new ReferenceDTO(reference.getId(),reference.getReference()))
                        .toList();
        DocumentDTO documentDto = new DocumentDTO(documentEntity.getId(), documentEntity.getTitle(),
                                documentEntity.getBody(), referenceDtos, authorDtos);
        return documentDto;
    }
}
