package com.example.library_api.service;

import com.example.library_api.domain.Book;
import com.example.library_api.domain.Category;
import com.example.library_api.dto.BookRequest;
import com.example.library_api.dto.BookResponse;
import com.example.library_api.dto.BooksPerCategoryDto;
import com.example.library_api.dto.TopAuthorDto;
import com.example.library_api.exception.BadRequestException;
import com.example.library_api.exception.ResourceNotFoundException;
import com.example.library_api.repository.BookRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorService authorService;

    public BookService(BookRepository bookRepository,
                       AuthorService authorService) {
        this.bookRepository = bookRepository;
        this.authorService = authorService;
    }

    private void validateYear(Integer year) {
        int current = Year.now().getValue();
        if (year < 1450 || year > current) {
            throw new BadRequestException("year doit être entre 1450 et " + current);
        }
    }

    public Page<BookResponse> search(
            String title,
            Long authorId,
            Category category,
            Integer yearFrom,
            Integer yearTo,
            Pageable pageable
    ) {
        Page<Book> page = bookRepository.search(title, authorId, category, yearFrom, yearTo, pageable);
        return page.map(this::toResponse);
    }

    public BookResponse getById(Long id) {
        Book b = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Livre " + id + " non trouvé"));
        return toResponse(b);
    }

    public BookResponse create(BookRequest request) {
        validateYear(request.year());
        Book book = new Book();
        book.setTitle(request.title());
        book.setIsbn(request.isbn());
        book.setYear(request.year());
        book.setCategory(request.category());
        book.setAuthor(authorService.getEntity(request.authorId()));
        return toResponse(bookRepository.save(book));
    }

    public BookResponse update(Long id, BookRequest request) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Livre " + id + " non trouvé"));

        validateYear(request.year());
        book.setTitle(request.title());
        book.setIsbn(request.isbn());
        book.setYear(request.year());
        book.setCategory(request.category());
        book.setAuthor(authorService.getEntity(request.authorId()));

        return toResponse(bookRepository.save(book));
    }

    public void delete(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new ResourceNotFoundException("Livre " + id + " non trouvé");
        }
        bookRepository.deleteById(id);
    }

    public List<BooksPerCategoryDto> getBooksPerCategory() {
        return bookRepository.countBooksByCategory().stream()
                .map(arr -> new BooksPerCategoryDto(
                        (Category) arr[0],
                        (Long) arr[1]
                ))
                .toList();
    }

    public List<TopAuthorDto> getTopAuthors(int limit) {
        return bookRepository.countBooksByAuthor().stream()
                .limit(limit)
                .map(arr -> {
                    var author = (com.example.library_api.domain.Author) arr[0];
                    long count = (Long) arr[1];
                    return new TopAuthorDto(
                            author.getId(),
                            author.getFirstName(),
                            author.getLastName(),
                            count
                    );
                })
                .toList();
    }

    private BookResponse toResponse(Book b) {
        return new BookResponse(
                b.getId(),
                b.getTitle(),
                b.getIsbn(),
                b.getYear(),
                b.getCategory(),
                b.getAuthor().getId(),
                b.getAuthor().getFirstName(),
                b.getAuthor().getLastName()
        );
    }
}
