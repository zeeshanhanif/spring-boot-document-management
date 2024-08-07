package com.app.documentmanagement.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import jakarta.validation.constraints.NotEmpty;
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
public class ReferenceDTO {
    
    private long id;

    @NotEmpty(message = "Reference is mandatory")
    private String reference;
    
    public ReferenceDTO(String reference) {
        this.reference = reference;
    }

    
}
