package com.app.documentmanagement.services;

import java.util.List;

import com.app.documentmanagement.dto.DocumentDTO;
import com.app.documentmanagement.entities.Document;

public interface DocumentService {
    
    DocumentDTO saveDocument(DocumentDTO documentDto);
    List<DocumentDTO> getAllDocuments();
    DocumentDTO getDocumentById(long id);
    DocumentDTO updateDocument(long id, DocumentDTO documentDto);
    boolean deleteDocumentById(long id);
    DocumentDTO convertEntityToDTO(Document documentEntity);
    Document convertToDocumentEntityFromDoucmentDTO(DocumentDTO documentDto);
}
