package com.example.library_api.dto;

import com.example.library_api.domain.Category; // <- Ajouté ici

public record BookResponse(
        Long id,
        String title,
        String isbn,
        Integer year,
        Category category,       // enum
        Long authorId,
        String authorFirstName,
        String authorLastName
) {}
