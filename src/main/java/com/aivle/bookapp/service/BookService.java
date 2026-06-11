package com.aivle.bookapp.service;

import com.aivle.bookapp.domain.Book;
import com.aivle.bookapp.dto.BookRequestDto;
import com.aivle.bookapp.dto.BookResponseDto;
import com.aivle.bookapp.repository.BookRepository;
import com.aivle.bookapp.exception.BookNotFoundException; 
import org.springframework.dao.DataIntegrityViolationException;
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
        // [수정] 입력 유효성 검사 (null 및 빈 문자열 검증)
        validateBookData(requestDto.getTitle(), requestDto.getAuthor(), requestDto.getDescription());

        Book book = new Book(
            requestDto.getTitle(),
            requestDto.getAuthor(),
            requestDto.getDescription(),
            requestDto.getUsersId()
        );
        
        try {
            Book savedBook = bookRepository.save(book);
            return new BookResponseDto(savedBook);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("도서 저장 중 데이터베이스 오류가 발생했습니다. 입력값을 확인해주세요.", e);
        }
    }

    // 2. 도서 목록 조회 (GET) - 검색 기능
    @Transactional(readOnly = true)
    public List<BookResponseDto> getBookList(String keyword) {
        List<Book> books;

        if (keyword != null && !keyword.trim().isEmpty()) {
            books = bookRepository.findByTitleContaining(keyword);
        } else {
            books = bookRepository.findAll();
        }

        return books.stream()
                .map(BookResponseDto::new)
                .collect(Collectors.toList());
    }

    // 3. 도서 상세 조회 (GET)
    @Transactional(readOnly = true)
    public BookResponseDto getBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
        return new BookResponseDto(book);
    }

    // 4. 도서 부분 수정 (PATCH)
    @Transactional
    public BookResponseDto updateBook(Long id, BookRequestDto requestDto) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));

        // [수정] 부분 수정 시 데이터 유효성 검증
        if (requestDto.getTitle() != null) {
            if (requestDto.getTitle().trim().isEmpty()) throw new IllegalArgumentException("제목을 빈칸으로 수정할 수 없습니다.");
            book.setTitle(requestDto.getTitle());
        }
        if (requestDto.getAuthor() != null) {
            if (requestDto.getAuthor().trim().isEmpty()) throw new IllegalArgumentException("저자를 빈칸으로 수정할 수 없습니다.");
            book.setAuthor(requestDto.getAuthor());
        }
        if (requestDto.getDescription() != null) {
            if (requestDto.getDescription().trim().isEmpty()) throw new IllegalArgumentException("본문 내용을 빈칸으로 수정할 수 없습니다.");
            book.setDescription(requestDto.getDescription());
        }

        return new BookResponseDto(book);
    }

    // 5. 도서 삭제 (DELETE)
    @Transactional
    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
        
        try {
            bookRepository.delete(book);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("해당 도서를 참조하고 있는 데이터가 있어 삭제할 수 없습니다.", e);
        }
    }

    // --- 공통 검증 메서드 (Service 내부용) ---
    private void validateBookData(String title, String author, String description) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("도서 제목은 필수 입력 항목입니다.");
        }
        if (author == null || author.trim().isEmpty()) {
            throw new IllegalArgumentException("저자는 필수 입력 항목입니다.");
        }
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("본문 내용은 필수 입력 항목입니다.");
        }
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