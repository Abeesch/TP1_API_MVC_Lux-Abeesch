package com.example.library_api.dto;

public record TopAuthorDto(
        Long authorId,
        String firstName,
        String lastName,
        long booksCount
) {}
