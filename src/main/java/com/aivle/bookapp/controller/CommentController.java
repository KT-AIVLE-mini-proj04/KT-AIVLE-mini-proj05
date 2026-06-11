package com.aivle.bookapp.controller;

import com.aivle.bookapp.dto.CommentCreateRequest;
import com.aivle.bookapp.dto.CommentResponse;
import com.aivle.bookapp.dto.CommentUpdateRequest;
import com.aivle.bookapp.service.CommentService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    // POST /comment  Authorization: Bearer <token>  body: { "bookId": Integer, "content": String }
    @PostMapping
    public ResponseEntity<CommentResponse> createComment(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @Valid @RequestBody CommentCreateRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(commentService.createComment(authorizationHeader, request));
    }

    // PATCH /comment/{id}  Authorization: Bearer <token>  body: { "content": String }
    @PatchMapping("/{id}")
    public ResponseEntity<CommentResponse> updateComment(
            @PathVariable Integer id,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @Valid @RequestBody CommentUpdateRequest request
    ) {
        return ResponseEntity.ok(commentService.updateComment(id, authorizationHeader, request));
    }

    // DELETE /comment/{id}  Authorization: Bearer <token>
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Integer id,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader
    ) {
        commentService.deleteComment(id, authorizationHeader);
        return ResponseEntity.noContent().build();
    }

    // GET /comment?bookId={bookId}          → 전체 조회
    // GET /comment?bookId={bookId}&page={page} → 페이지 조회 (10개씩)
    @GetMapping
    public ResponseEntity<List<CommentResponse>> getComments(
            @RequestParam Integer bookId,
            @RequestParam(required = false) Integer page
    ) {
        return ResponseEntity.ok(commentService.getCommentsByBookId(bookId, page));
    }
}
