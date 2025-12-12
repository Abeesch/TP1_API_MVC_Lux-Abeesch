package com.example.library_api.dto;

import com.example.library_api.domain.Category;

public record BooksPerCategoryDto(
        Category category,
        long count
) {}