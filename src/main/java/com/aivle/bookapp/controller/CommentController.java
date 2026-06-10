package com.aivle.bookapp.controller;

import com.aivle.bookapp.dto.CommentCreateRequest;
import com.aivle.bookapp.dto.CommentResponse;
import com.aivle.bookapp.dto.CommentUpdateRequest;
import com.aivle.bookapp.service.CommentService;
import jakarta.validation.Valid;
import java.util.List;
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

    // POST /comment  body: { "bookId": Integer, "content": String }
    @PostMapping
    public ResponseEntity<CommentResponse> createComment(
            @RequestParam Integer usersId,
            @Valid @RequestBody CommentCreateRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(commentService.createComment(usersId, request));
    }

    // PATCH /comment/{id}  body: { "content": String }
    @PatchMapping("/{id}")
    public ResponseEntity<CommentResponse> updateComment(
            @PathVariable Integer id,
            @RequestParam Integer usersId,
            @Valid @RequestBody CommentUpdateRequest request
    ) {
        return ResponseEntity.ok(commentService.updateComment(id, usersId, request));
    }

    // DELETE /comment/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Integer id,
            @RequestParam Integer usersId
    ) {
        commentService.deleteComment(id, usersId);
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
