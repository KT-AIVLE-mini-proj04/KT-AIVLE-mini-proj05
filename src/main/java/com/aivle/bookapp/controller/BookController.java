package com.aivle.bookapp.controller;

import com.aivle.bookapp.dto.BookResponseDto;
import com.aivle.bookapp.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    // AI 표지 저장: PATCH /api/books/{id}/cover
    @PatchMapping("/{id}/cover")
    public ResponseEntity<BookResponseDto> updateCover(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        String coverImageUrl = body.get("coverImageUrl");
        return ResponseEntity.ok(bookService.updateCover(id, coverImageUrl));
    }
}
