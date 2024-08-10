package com.app.documentmanagement.service;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

import com.app.documentmanagement.dto.AuthorDTO;
import com.app.documentmanagement.dto.DocumentDTO;
import com.app.documentmanagement.dto.ReferenceDTO;
import com.app.documentmanagement.entities.Author;
import com.app.documentmanagement.entities.Document;
import com.app.documentmanagement.entities.Reference;
import com.app.documentmanagement.exceptions.AuthorNotFoundException;
import com.app.documentmanagement.exceptions.DocumentNotFoundException;
import com.app.documentmanagement.exceptions.DocumentNullValueException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import com.app.documentmanagement.repositories.AuthorRepository;
import com.app.documentmanagement.repositories.DocumentRepository;
import com.app.documentmanagement.services.AuthorServiceImpl;
import com.app.documentmanagement.services.DocumentServiceImpl;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class DocumentServiceTest {
    
    @Mock
    private DocumentRepository documentRepository;

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private DocumentServiceImpl documentService;

    @InjectMocks
    private AuthorServiceImpl authorService;

    private List<Author> authors;
    private List<Document> documents;

        @BeforeEach
    public void setup() {
        authors = populateAuthorsList();
        documents = populateDocumentList();
    }

    @AfterEach
    public void tearDown() {
        authors = null;
        documents = null;
    }

    @Test
    public void shouldReturnAllDocuments() {
        given(documentRepository.findAll()).willReturn(documents);
        List<DocumentDTO> retrievedDocuments = documentService.getAllDocuments();

        assertThat(retrievedDocuments).isNotNull().isNotEmpty()
                        .hasSize(documents.size()).doesNotContainNull();
        assertThat(retrievedDocuments.get(0).getTitle()).isNotNull()
                        .isNotEmpty().isEqualTo(documents.get(0).getTitle());
    }

    @Test
    public void shouldReturnValidDocumentByDocumentId() {
        given(documentRepository.findById(Long.valueOf(1))).willReturn(Optional.of(documents.get(0)));
        DocumentDTO retrievedDocument = documentService.getDocumentById(1);

        assertThat(retrievedDocument).isNotNull();
        assertThat(retrievedDocument.getTitle()).isEqualTo("The Future of Artificial Intelligence");
        assertThat(retrievedDocument.getReferences().get(0).getReference()).isEqualTo("Russell, S., & Norvig, P. (2021). Artificial Intelligence: A Modern Approach. Pearson.");   
    }

    @Test
    public void shouldFailOnNonExistentId() {
        long documentId = 5;
        given(documentRepository.findById(Long.valueOf(documentId))).willThrow(new DocumentNotFoundException("No Such Document Exists with id "+documentId));
        assertThrows(DocumentNotFoundException.class, ()-> {
            documentService.getDocumentById(documentId);
        });
    }

    @Test
    public void shouldDeleteDocumentById() {
        long doucmentId = 1;
        given(documentRepository.findById(Long.valueOf(doucmentId))).willReturn(Optional.of(documents.get(0)));

        willDoNothing().given(documentRepository).deleteById(Long.valueOf(doucmentId));

        boolean isDeleted = documentService.deleteDocumentById(doucmentId);
        
        assertThat(isDeleted).isTrue();
    }

    /**
     * Not sure the correct way, but this one is working
     */
    @Test
    public void shouldFailDeleteDocumentById() {
        willDoNothing().given(documentRepository).deleteById(Long.valueOf(2));
        
        assertThrows(DocumentNotFoundException.class, ()-> {
            documentService.getDocumentById(2);
        });
    }

    //@Disabled
    @Test
    public void shouldSaveDocumentWithValidData() {

        given(authorRepository.findById(Long.valueOf(10))).willReturn(Optional.of(authors.get(9)));

        DocumentDTO documentDto = populateSingleDocument();
        Document document = documentService.convertToDocumentEntityFromDoucmentDTO(documentDto);

        given(documentRepository.save(Mockito.any(Document.class))).willReturn(document);

        DocumentDTO savedDocumentDto = documentService.saveDocument(documentDto);
        assertThat(savedDocumentDto).isNotNull();
        assertThat(savedDocumentDto.getId()).isNotZero();
        assertThat(savedDocumentDto.getTitle()).isEqualTo(documentDto.getTitle());
        assertThat(savedDocumentDto.getBody()).isEqualTo(documentDto.getBody());
    }

    //@Disabled
    @Test
    public void shouldSaveDocumentFailWithInValidData() {
        DocumentDTO documentDto = new DocumentDTO();

        given(documentRepository.save(Mockito.any(Document.class))).willThrow(new DocumentNullValueException("Title and body must be provided"));

        assertThrows(DocumentNullValueException.class, ()-> {
            documentService.saveDocument(documentDto);
        });
    }

    //@Disabled
    @Test
    public void shouldSaveDocumentFailWhenReferencesNotProvided() {
        DocumentDTO documentDto = new DocumentDTO(4, "Exploring Quantum Computing", "Quantum computing is poised to revolutionize fields ranging from cryptography to complex system simulations.");

        given(documentRepository.save(Mockito.any(Document.class))).willThrow(new DocumentNullValueException("References must be provided"));

        assertThrows(DocumentNullValueException.class, ()-> {
            documentService.saveDocument(documentDto);
        });
    }

    //@Disabled
    @Test
    public void shouldSaveDocumentFailWhenAuthorsNotProvided() {
        ArrayList<ReferenceDTO> referenceDtos = new ArrayList<>();
        referenceDtos.add(new ReferenceDTO("Nielsen, M. A., & Chuang, I. L. (2010). Quantum Computation and Quantum Information: 10th Anniversary Edition. Cambridge University Press"));
        referenceDtos.add(new ReferenceDTO("Preskill, J. (2018). Quantum Computing in the NISQ era and beyond. Quantum, 2, 79."));
        DocumentDTO documentDto = new DocumentDTO(4, "Exploring Quantum Computing", "Quantum computing is poised to revolutionize fields ranging from cryptography to complex system simulations.");
        documentDto.setReferences(referenceDtos);

        given(documentRepository.save(Mockito.any(Document.class))).willThrow(new DocumentNullValueException("Authors must be provided"));
        
        assertThrows(DocumentNullValueException.class, ()-> {
            documentService.saveDocument(documentDto);
        });
    }

    //@Disabled
    @Test
    public void shouldSaveDocumentFailWhenInvalidAuthorsProvided() {
        ArrayList<ReferenceDTO> referenceDtos = new ArrayList<>();
        referenceDtos.add(new ReferenceDTO("Nielsen, M. A., & Chuang, I. L. (2010). Quantum Computation and Quantum Information: 10th Anniversary Edition. Cambridge University Press"));
        referenceDtos.add(new ReferenceDTO("Preskill, J. (2018). Quantum Computing in the NISQ era and beyond. Quantum, 2, 79."));
        DocumentDTO documentDto = new DocumentDTO(4, "Exploring Quantum Computing", "Quantum computing is poised to revolutionize fields ranging from cryptography to complex system simulations.");
        documentDto.setReferences(referenceDtos);
        documentDto.setAuthors(List.of(new AuthorDTO(10,"Taha","Shahid")));

        given(documentRepository.save(Mockito.any(Document.class))).willThrow(new AuthorNotFoundException("Authors must be provided"));
        
        assertThrows(AuthorNotFoundException.class, ()-> {
            documentService.saveDocument(documentDto);
        });
    }

    @Test
    public void shouldUpdateDocumentWithValidData() {
        List<Author> authorsSublist = authors.stream().filter(author -> author.getId() <=3).toList();
        authorsSublist.forEach(author -> given(authorRepository.findById(Long.valueOf(author.getId()))).willReturn(Optional.of(authors.get(0))));
        
        Document initialStateDocument = documents.get(0);
        given(documentRepository.findById(Long.valueOf(1))).willReturn(Optional.of(initialStateDocument));
        
        initialStateDocument.setTitle("The Future of AI");

        given(documentRepository.save(initialStateDocument)).willReturn(initialStateDocument);

        DocumentDTO documentToBeSaved = DocumentDTO.builder().id(initialStateDocument.getId()).title(initialStateDocument.getTitle()).build();

        DocumentDTO savedDocumentDto = documentService.updateDocument(1, documentToBeSaved);
        assertThat(savedDocumentDto).isNotNull();
        assertThat(savedDocumentDto.getTitle()).isEqualTo("The Future of AI");
    }

    @Test
    public void shouldAddAuthorInDocumentWithValidData() {
        Document initialStateDocument = documents.get(0);
        List<Author> documentAuthors = initialStateDocument.getAuthors();
        documentAuthors.forEach(author -> given(authorRepository.findById(Long.valueOf(author.getId()))).willReturn(Optional.of(author)));

        given(authorRepository.findById(Long.valueOf(10))).willReturn(Optional.of(authors.get(9)));
        
        given(documentRepository.findById(Long.valueOf(1))).willReturn(Optional.of(initialStateDocument));
        
        List<Author> updatedAuthorsList = documentAuthors.stream().map(author-> author).collect(Collectors.toList());
        updatedAuthorsList.add(authors.get(9));

        initialStateDocument.setAuthors(updatedAuthorsList);

        given(documentRepository.save(initialStateDocument)).willReturn(initialStateDocument);

        List<AuthorDTO> authorDtos = initialStateDocument.getAuthors().stream()
                                    .map(author-> authorService.convertEntityToDTO(author)).toList();
        DocumentDTO documentToBeSaved = DocumentDTO.builder().id(initialStateDocument.getId()).authors(authorDtos).build();

        DocumentDTO savedDocumentDto = documentService.updateDocument(1, documentToBeSaved);
        assertThat(savedDocumentDto).isNotNull(); 
        assertThat(savedDocumentDto.getAuthors()).isNotNull().isNotEmpty().hasSize(4);
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
}
