package com.example.library_api.dto;

import com.example.library_api.domain.Category;
import jakarta.validation.constraints.*;

public record BookRequest(
        @NotBlank
        String title,

        @NotBlank
        @Pattern(regexp = "^[0-9\\-]{10,17}$", message = "isbn format incorrect")
        String isbn,

        @NotNull
        Integer year,

        @NotNull
        Category category,

        @NotNull
        Long authorId
) {}