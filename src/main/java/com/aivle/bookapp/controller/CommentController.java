package com.aivle.bookapp.controller;

import com.aivle.bookapp.dto.CommentCreateRequest;
import com.aivle.bookapp.dto.CommentResponse;
import com.aivle.bookapp.dto.CommentUpdateRequest;
import com.aivle.bookapp.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    // POST /comment  body: { "bookId": Long, "content": String }
    @PostMapping
    public ResponseEntity<CommentResponse> createComment(
        @RequestParam Long userId, // TODO: 인증 구현 후 토큰에서 추출
        @Valid @RequestBody CommentCreateRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(commentService.createComment(userId, request));
    }

    // PATCH /comment/{id}  body: { "content": String }
    @PatchMapping("/{id}")
    public ResponseEntity<CommentResponse> updateComment(
        @PathVariable String id,
        @RequestParam Long userId, // TODO: 인증 구현 후 토큰에서 추출
        @Valid @RequestBody CommentUpdateRequest request
    ) {
        return ResponseEntity.ok(commentService.updateComment(id, userId, request));
    }

    // DELETE /comment/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(
        @PathVariable String id,
        @RequestParam Long userId // TODO: 인증 구현 후 토큰에서 추출
    ) {
        commentService.deleteComment(id, userId);
        return ResponseEntity.noContent().build();
    }

    // GET /comment?bookId={bookId}
    @GetMapping
    public ResponseEntity<List<CommentResponse>> getComments(
        @RequestParam Long bookId
    ) {
        return ResponseEntity.ok(commentService.getCommentsByBookId(bookId));
    }
}
