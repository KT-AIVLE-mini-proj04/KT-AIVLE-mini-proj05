package com.aivle.bookapp.dto;

import com.aivle.bookapp.domain.Book;
import java.time.LocalDateTime;

public class BookResponseDto {
    private Long bookId;
    private String title;
    private String author;
    private String description;
    private String cover;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Entity(DB 데이터)를 DTO로 변환하는 생성자
    public BookResponseDto(Book book) {
        this.bookId = book.getBookId();
        this.title = book.getTitle();
        this.author = book.getAuthor();
        this.description = book.getDescription();
        this.cover = book.getCover();
        this.createdAt = book.getCreatedAt();
        this.updatedAt = book.getUpdatedAt();
    }

    // --- Getter --- (응답용이므로 보통 Getter만 사용합니다)
    public Long getBookId() { return bookId; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getDescription() { return description; }
    public String getCover() { return cover; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}