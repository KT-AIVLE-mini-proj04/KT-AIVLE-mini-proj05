package com.aivle.bookapp.controller;

import com.aivle.bookapp.dto.BookRequestDto;
import com.aivle.bookapp.dto.BookResponseDto;
import com.aivle.bookapp.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books") // 명세서의 공통 URL 경로
public class BookController {

    private final BookService bookService;

    // 의존성 주입 (Service 연결)
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    // 1. 도서 등록 (Create)
    // 기능: Books 등록 | Method: POST | URL: /books
    @PostMapping
    public ResponseEntity<BookResponseDto> createBook(@RequestBody BookRequestDto requestDto) {
        BookResponseDto responseDto = bookService.createBook(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    // 2. 도서 목록 조회 (Read - List)
    // 기능: Books 목록 조회 | Method: GET | URL: /books
    @GetMapping
    public ResponseEntity<List<BookResponseDto>> getBookList() {
        List<BookResponseDto> list = bookService.getBookList();
        return ResponseEntity.ok().body(list);
    }

    // 3. 도서 상세 조회 (Read - Single)
    // 기능: Books 단일 항목 조회 | Method: GET | URL: /books/:id
    @GetMapping("/{id}")
    public ResponseEntity<BookResponseDto> getBookDetail(@PathVariable("id") Long id) {
        BookResponseDto responseDto = bookService.getBook(id);
        return ResponseEntity.ok().body(responseDto);
    }

    // 4. 도서 부분 수정 (Update - Partial)
    // 기능: Books 수정 (변경 필드만) | Method: PATCH | URL: /books/:id
    @PatchMapping("/{id}")
    public ResponseEntity<BookResponseDto> updateBook(
            @PathVariable("id") Long id, 
            @RequestBody BookRequestDto requestDto) {
        BookResponseDto responseDto = bookService.updateBook(id, requestDto);
        return ResponseEntity.ok().body(responseDto);
    }

    // 5. 도서 삭제 (Delete)
    // 기능: Books 삭제 | Method: DELETE | URL: /books/:id
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable("id") Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.ok().body("도서 ID " + id + "가 성공적으로 삭제되었습니다.");
    }
}