package com.example.library_api.controller;

import com.example.library_api.dto.BooksPerCategoryDto;
import com.example.library_api.dto.TopAuthorDto;
import com.example.library_api.service.BookService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stats")
public class StatsController {

    private final BookService bookService;
    public StatsController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/books-per-category")
    public List<BooksPerCategoryDto> booksPerCategory() {
        return bookService.getBooksPerCategory();
    }

    @GetMapping("/top-authors")
    public List<TopAuthorDto> topAuthors(@RequestParam(defaultValue = "3") int limit) {
        return bookService.getTopAuthors(limit);
    }
}