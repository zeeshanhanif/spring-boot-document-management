package com.app.documentmanagement.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIgnoreType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonInclude(Include.NON_NULL)
public class DocumentDTO {
    
    private long id;
    private String title;
    private String body;
    private List<String> references;
    private List<AuthorDTO> authors;
    
    public DocumentDTO(long id, String title, String body) {
        this.id = id;
        this.title = title;
        this.body = body;
    }

    public DocumentDTO(long id, String title, String body, List<String> references) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.references = references;
    }

}
