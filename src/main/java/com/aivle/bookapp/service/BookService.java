package com.aivle.bookapp.service;

import com.aivle.bookapp.domain.Book;
import com.aivle.bookapp.dto.BookResponseDto;
import com.aivle.bookapp.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Transactional
    public BookResponseDto updateCover(Long id, String coverImageUrl) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 도서가 존재하지 않습니다. id=" + id));
        book.setCoverImageUrl(coverImageUrl);
        return new BookResponseDto(book);
    }
}
