package com.example.library_api.service;

import com.example.library_api.domain.Author;
import com.example.library_api.dto.AuthorDto;
import com.example.library_api.exception.ResourceNotFoundException;
import com.example.library_api.repository.AuthorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public List<AuthorDto> findAll() {
        return authorRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    public AuthorDto findById(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Auteur " + id + " non trouvé"));
        return toDto(author);
    }

    public AuthorDto create(AuthorDto dto) {
        Author author = new Author();
        author.setFirstName(dto.firstName());
        author.setLastName(dto.lastName());
        author.setBirthYear(dto.birthYear());
        return toDto(authorRepository.save(author));
    }

    public AuthorDto update(Long id, AuthorDto dto) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Auteur " + id + " non trouvé"));
        author.setFirstName(dto.firstName());
        author.setLastName(dto.lastName());
        author.setBirthYear(dto.birthYear());
        return toDto(authorRepository.save(author));
    }

    public void delete(Long id) {
        if (!authorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Auteur " + id + " non trouvé");
        }
        authorRepository.deleteById(id);
    }

    public Author getEntity(Long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Auteur " + id + " non trouvé"));
    }

    private AuthorDto toDto(Author a) {
        return new AuthorDto(a.getId(), a.getFirstName(), a.getLastName(), a.getBirthYear());
    }
}