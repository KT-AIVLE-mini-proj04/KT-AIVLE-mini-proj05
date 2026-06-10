package com.aivle.bookapp.controller;

import com.aivle.bookapp.dto.BookLikeResponse;
import com.aivle.bookapp.service.BookLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookLikeController {

    private final BookLikeService bookLikeService;

    @PostMapping("/{bookId}/likes")
    public ResponseEntity<BookLikeResponse> toggleLike(
            @PathVariable Long bookId) {
//            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        // SecurityContext에 저장된 현재 로그인 유저의 ID 추출
//        Long userId = userDetails.getUserId();

        BookLikeResponse response = bookLikeService.toggleLike(bookId); // userId

        return ResponseEntity.ok(response);
    }
    }