package com.aivle.bookapp.controller;

import com.aivle.bookapp.dto.BookCountResponseDto;
import com.aivle.bookapp.dto.BookRequestDto;
import com.aivle.bookapp.dto.BookResponseDto;
import com.aivle.bookapp.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

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
    // 2. @Valid 추가: 프론트엔드에서 넘어온 데이터를 DB에 넣기 전에 DTO의 규칙(필수입력, 길이제한)대로 검사함
    public ResponseEntity<BookResponseDto> createBook(@Valid @RequestBody BookRequestDto requestDto) {
        BookResponseDto responseDto = bookService.createBook(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    // 2. 도서 목록 조회 (Read - List)
    // 기능: Books 목록 조회 | Method: GET | URL: /books
    @GetMapping
    // 3. 헤더의 검색 바 기능을 위해 'keyword' 파라미터 추가
    // @RequestParam(required = false)는 "검색어가 필수는 아니다(없으면 전체조회)"라는 뜻입니다.
    public ResponseEntity<List<BookResponseDto>> getBookList(@RequestParam(required = false) String keyword) {
    // 4. Service 쪽에 검색어가 있는지 없는지 같이 넘겨주도록 수정
        List<BookResponseDto> list = bookService.getBookList(keyword);
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/count")
    public ResponseEntity<BookCountResponseDto> getBookCounts() {
        BookCountResponseDto responseDto = bookService.getBookCounts();
        return ResponseEntity.ok().body(responseDto);
    }

    // 3. 도서 상세 조회 (Read - Single)
    // 기능: Books 단일 항목 조회 | Method: GET | URL: /books/:id
    @GetMapping("/{id}")
    public ResponseEntity<BookResponseDto> getBookDetail(@PathVariable("id") Long id) {
        BookResponseDto responseDto = bookService.getBook(id);
        return ResponseEntity.ok().body(responseDto);
    }

    // 4. 도서 부분 수정 (Update - Partial)
    // 기능: Books 수정 | Method: PATCH | URL: /books/:id
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

    // 6. AI 표지 이미지 URL 저장 (Update - Cover)
    // 기능: AI 생성 표지 URL 저장 | Method: PATCH | URL: /books/:id/cover
    @PatchMapping("/{id}/cover")
    public ResponseEntity<BookResponseDto> updateCover(
            @PathVariable("id") Long id,
            @RequestBody BookRequestDto requestDto) {
        BookResponseDto responseDto = bookService.updateCover(id, requestDto.getCover());
        return ResponseEntity.ok().body(responseDto);
    }
}
