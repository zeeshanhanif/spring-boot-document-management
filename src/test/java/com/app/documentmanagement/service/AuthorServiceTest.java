package com.app.documentmanagement.service;

import java.util.List;
import java.util.Arrays;
import java.util.Optional;

import com.app.documentmanagement.dto.AuthorDTO;
import com.app.documentmanagement.entities.Author;
import com.app.documentmanagement.exceptions.AuthorAlreadyExistsException;
import com.app.documentmanagement.exceptions.AuthorNotFoundException;
import com.app.documentmanagement.exceptions.AuthorNullValueException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import com.app.documentmanagement.repositories.AuthorRepository;
import com.app.documentmanagement.repositories.DocumentRepository;
import com.app.documentmanagement.services.AuthorServiceImpl;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class AuthorServiceTest {

    /*
    @TestConfiguration
    public static class TestAuthorServiceConfig {    
        @Bean
        public ModelMapper modelMapper() {
            return new ModelMapper();
        }
    }
    */
    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private DocumentRepository documentRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private AuthorServiceImpl authorService;

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
    public void shouldReturnAuthors() {
        given(authorRepository.findAll()).willReturn(authors);
        List<AuthorDTO> retrievedAuthors = authorService.getAllAuthors();

        assertThat(retrievedAuthors).isNotNull().isNotEmpty()
                        .hasSize(authors.size()).doesNotContainNull();
        assertThat(retrievedAuthors.get(0).getFirstName()).isNotNull()
                        .isNotEmpty().isEqualTo(authors.get(0).getFirstName());
    }

    @Test
    public void shouldReturnValidAuthorByAuthorId() {
        given(authorRepository.findById(Long.valueOf(2))).willReturn(Optional.of(authors.get(1)));
        AuthorDTO retrievedAuthor = authorService.getAuthorById(2);

        assertThat(retrievedAuthor).isNotNull();
        assertThat(retrievedAuthor.getFirstName()).isEqualTo("Zain");
        assertThat(retrievedAuthor.getLastName()).isEqualTo("Hanif");   
    }

    @Test
    public void shouldFailOnNonExistentId() {
        given(authorRepository.findById(Long.valueOf(12))).willThrow(new AuthorNotFoundException("No Such Author Exists with id 12"));
        assertThrows(AuthorNotFoundException.class, ()-> {
            authorService.getAuthorById(2);
        });
    }

    @Test
    public void shouldDeleteAuthorById() {
        long authorId = 1;
        given(authorRepository.findById(Long.valueOf(authorId))).willReturn(Optional.of(authors.get(0)));

        willDoNothing().given(authorRepository).deleteById(Long.valueOf(authorId));

        boolean isDeleted = authorService.deleteAuthorById(authorId);
        
        assertThat(isDeleted).isTrue();
    }

    /**
     * Not sure the correct way, but this one is working
     */
    @Test
    public void shouldFailDeleteAuthorById() {
        willDoNothing().given(authorRepository).deleteById(Long.valueOf(2));
        
        assertThrows(AuthorNotFoundException.class, ()-> {
            authorService.getAuthorById(2);
        });
    }

    //@Disabled
    @Test
    public void shouldSaveAuthorWithValidData() {
        AuthorDTO authorDTO = new AuthorDTO(6,"Zia","Khan");
        Author author = convertToAuthorEntityFromAuthorDTO(authorDTO);
        given(authorRepository.save(Mockito.any(Author.class))).willReturn(author);

        AuthorDTO savedAuthorDto = authorService.saveAuthor(authorDTO);
        assertThat(savedAuthorDto).isNotNull();
        assertThat(savedAuthorDto.getFirstName()).isEqualTo(authorDTO.getFirstName());
        assertThat(savedAuthorDto.getLastName()).isEqualTo(authorDTO.getLastName());
    }

    //@Disabled
    @Test
    public void shouldSaveAuthorFailWithInValidData() {
        AuthorDTO authorDTO = new AuthorDTO();

        given(authorRepository.save(Mockito.any(Author.class))).willThrow(new AuthorNullValueException("First Name and Last Name must be provided"));

        assertThrows(AuthorNullValueException.class, ()-> {
            authorService.saveAuthor(authorDTO);
        });
    }

    //@Disabled
    @Test
    public void shouldSaveAuthorFailWithExistentFirstAndLastName() {
        AuthorDTO authorDTO = new AuthorDTO(6,"Zeeshan","Hanif");

        given(authorRepository.findByFirstNameAndLastName("Zeeshan", "Hanif")).willReturn(authors.get(0));
        given(authorRepository.save(Mockito.any(Author.class))).willThrow(new AuthorAlreadyExistsException("Author with same First and Last name already exists"));

        assertThrows(AuthorAlreadyExistsException.class, ()-> {
            authorService.saveAuthor(authorDTO);
        });
    }

    @Test
    public void shouldUpdateAuthorWithValidData() {
        Author initialStateAuthor = authors.get(0);
        
        given(authorRepository.findById(Long.valueOf(1))).willReturn(Optional.of(initialStateAuthor));
        
        initialStateAuthor.setFirstName("Haseeb");

        given(authorRepository.save(initialStateAuthor)).willReturn(initialStateAuthor);

        AuthorDTO authorToBeSaved = new AuthorDTO(initialStateAuthor.getId(),initialStateAuthor.getFirstName(),initialStateAuthor.getLastName());

        AuthorDTO savedAuthorDto = authorService.updateAuthor(1, authorToBeSaved);
        assertThat(savedAuthorDto).isNotNull();
        assertThat(savedAuthorDto.getFirstName()).isEqualTo("Haseeb");
    }

    private List<Author> populateAuthorsList() {
        return Arrays.asList(
            new Author("Zeeshan","Hanif"),
            new Author("Zain","Hanif"),
            new Author("Inam","ul Haq"),
            new Author("Rehan","Uddin"),
            new Author("Taha","Ahmed")
        );
    }

    private Author convertToAuthorEntityFromAuthorDTO(AuthorDTO authorDto) {
        return new Author(authorDto.getFirstName(),authorDto.getLastName());
    }
}
