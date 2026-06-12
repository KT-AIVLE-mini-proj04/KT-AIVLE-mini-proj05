package com.aivle.bookapp.service;

import com.aivle.bookapp.domain.Book;
import com.aivle.bookapp.dto.BookRequestDto;
import com.aivle.bookapp.dto.BookResponseDto;
import com.aivle.bookapp.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {

    private final BookRepository bookRepository;

    // 의존성 주입 (Repository 연결)
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    // 1. 도서 등록 (POST)
    @Transactional
    public BookResponseDto createBook(BookRequestDto requestDto) {
        Book book = new Book(
            requestDto.getTitle(),
            requestDto.getAuthor(),
            requestDto.getDescription(),
            requestDto.getUsersId()
        );
        book.setCover(requestDto.getCoverImageUrl()); 
        Book savedBook = bookRepository.save(book);
        return new BookResponseDto(savedBook);
    }

    // 2. 도서 목록 조회 (GET) - 검색 기능 추가
    @Transactional(readOnly = true)
    // Controller에서 넘겨준 검색어(keyword) 파라미터를 받음
    public List<BookResponseDto> getBookList(String keyword) {
        List<Book> books;

    // 검색어가 비어있지 않다면 제목으로 검색, 없다면 기존처럼 전체 조회
        if (keyword != null && !keyword.trim().isEmpty()) {
            books = bookRepository.findByTitleContaining(keyword);
        } else {
            books = bookRepository.findAll();
        }

        return books.stream()
                .map(BookResponseDto::new) // 각 Book 엔티티를 DTO로 변환
                .collect(Collectors.toList());
    }

    // 3. 도서 상세 조회 (GET)
    @Transactional(readOnly = true)
    public BookResponseDto getBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 도서가 존재하지 않습니다. id=" + id));
        return new BookResponseDto(book);
    }

    // 4. 도서 부분 수정 (PATCH) - 명세서 핵심 요구사항
    @Transactional
    public BookResponseDto updateBook(Long id, BookRequestDto requestDto) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 도서가 존재하지 않습니다. id=" + id));

        // 들어온 값이 null이 아닐 때만 기존 값을 변경 (부분 수정)
        if (requestDto.getTitle() != null) book.setTitle(requestDto.getTitle());
        if (requestDto.getAuthor() != null) book.setAuthor(requestDto.getAuthor());
        if (requestDto.getDescription() != null) book.setDescription(requestDto.getDescription());
        if (requestDto.getCoverImageUrl() != null) book.setCover(requestDto.getCoverImageUrl());

        // @Transactional 덕분에 save()를 명시적으로 호출하지 않아도 변경 사항이 DB에 자동 반영됩니다.
        return new BookResponseDto(book);
    }

    // 5. 도서 삭제 (DELETE)
    @Transactional
    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 도서가 존재하지 않습니다. id=" + id));
        bookRepository.delete(book);
    }

    // 6. AI 표지 이미지 URL 저장 (PATCH)
    @Transactional
    public BookResponseDto updateCover(Long id, String coverUrl) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 도서가 존재하지 않습니다. id=" + id));
        book.setCover(coverUrl);
        return new BookResponseDto(book);
    }
}