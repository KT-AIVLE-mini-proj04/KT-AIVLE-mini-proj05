package com.aivle.bookapp.controller;

import com.aivle.bookapp.domain.Users;
import com.aivle.bookapp.dto.BookLikeResponse;
import com.aivle.bookapp.service.BookLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookLikeController {

    private final BookLikeService bookLikeService;

    @GetMapping("/{bookId}/likes")
    public ResponseEntity<BookLikeResponse> getLikeStatus(
            @PathVariable Long bookId,
            @AuthenticationPrincipal Users user
    ) {
        BookLikeResponse response =
                bookLikeService.getLikeStatus(bookId, user.getUsersId());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{bookId}/likes")
    public ResponseEntity<BookLikeResponse> toggleLike(
            @PathVariable Long bookId,
            @AuthenticationPrincipal Users user) {

        Integer usersId = user.getUsersId();

        BookLikeResponse response = bookLikeService.toggleLike(bookId, usersId); // userId

        return ResponseEntity.ok(response);
    }
    }