package com.aivle.bookapp.dto;

import com.aivle.bookapp.domain.Comment;

import java.time.LocalDateTime;

public class CommentResponse {

    private String commentId;
    private Long bookId;
    private Long userId;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private CommentResponse() {}

    public static CommentResponse from(Comment comment) {
        CommentResponse response = new CommentResponse();
        response.commentId = comment.getCommentId();
        response.bookId = comment.getBookId();
        response.userId = comment.getUserId();
        response.content = comment.getContent();
        response.createdAt = comment.getCreatedAt();
        response.updatedAt = comment.getUpdatedAt();
        return response;
    }

    public String getCommentId() { return commentId; }
    public Long getBookId() { return bookId; }
    public Long getUserId() { return userId; }
    public String getContent() { return content; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
