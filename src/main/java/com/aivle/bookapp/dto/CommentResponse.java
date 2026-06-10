package com.aivle.bookapp.dto;

import com.aivle.bookapp.domain.Comment;

import java.time.LocalDateTime;

public class CommentResponse {

    private Integer commentsId;
    private Integer bookId;
    private Integer usersId;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private CommentResponse() {}

    public static CommentResponse from(Comment comment) {
        CommentResponse response = new CommentResponse();
        response.commentsId = comment.getCommentsId();
        response.bookId = comment.getBookId();
        response.usersId = comment.getUsersId();
        response.content = comment.getContent();
        response.createdAt = comment.getCreatedAt();
        response.updatedAt = comment.getUpdatedAt();
        return response;
    }

    public Integer getCommentsId() { return commentsId; }
    public Integer getBookId() { return bookId; }
    public Integer getUsersId() { return usersId; }
    public String getContent() { return content; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
