package com.app.documentmanagement.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@JsonInclude(Include.NON_NULL)
public class DocumentDTO {
    
    private long id;

    @NotBlank(message = "Title is mandatory")
    private String title;

    @NotBlank(message = "Doucment body is mandatory")
    private String body;

    @Valid
    @NotEmpty(message = "References must not be empty")
    private List<ReferenceDTO> references;

    @NotEmpty(message = "Authors must not be empty")
    private List<AuthorDTO> authors;
    
    public DocumentDTO(long id, String title, String body) {
        this.id = id;
        this.title = title;
        this.body = body;
    }

    public DocumentDTO(long id, String title, String body, List<ReferenceDTO> references) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.references = references;
    }

}
