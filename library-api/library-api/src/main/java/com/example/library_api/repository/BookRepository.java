package com.example.library_api.repository;

import com.example.library_api.domain.Book;
import com.example.library_api.domain.Category;
import com.example.library_api.domain.Author;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("""
           SELECT b FROM Book b
           WHERE (:title IS NULL OR LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%')))
             AND (:authorId IS NULL OR b.author.id = :authorId)
             AND (:category IS NULL OR b.category = :category)
             AND (:yearFrom IS NULL OR b.year >= :yearFrom)
             AND (:yearTo IS NULL OR b.year <= :yearTo)
           """)
    Page<Book> search(
            @Param("title") String title,
            @Param("authorId") Long authorId,
            @Param("category") Category category,
            @Param("yearFrom") Integer yearFrom,
            @Param("yearTo") Integer yearTo,
            Pageable pageable
    );

    @Query("""
           SELECT b.category, COUNT(b)
           FROM Book b
           GROUP BY b.category
           """)
    List<Object[]> countBooksByCategory();

    @Query("""
           SELECT b.author, COUNT(b)
           FROM Book b
           GROUP BY b.author
           ORDER BY COUNT(b) DESC
           """)
    List<Object[]> countBooksByAuthor();
}