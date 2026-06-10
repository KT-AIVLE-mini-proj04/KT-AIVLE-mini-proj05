package com.aivle.bookapp.dto;

import com.aivle.bookapp.domain.Book;
import java.time.LocalDateTime;

public class BookResponseDto {
    private Long id;
    private String title;
    private String author;
    private String content;
    private String cover;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Entity(DB 데이터)를 DTO로 변환하는 생성자
    public BookResponseDto(Book book) {
        this.id = book.getBookId();
        this.title = book.getTitle();
        this.author = book.getAuthor();
        this.content = book.getDescription();
        this.cover = book.getCover();
        this.createdAt = book.getCreatedAt();
        this.updatedAt = book.getUpdatedAt();
    }

    // --- Getter --- (응답용이므로 보통 Getter만 사용합니다)
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getContent() { return content; }
    public String getCover() { return cover; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}