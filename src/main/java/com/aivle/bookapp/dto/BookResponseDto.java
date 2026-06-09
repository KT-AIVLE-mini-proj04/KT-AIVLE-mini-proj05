package com.aivle.bookapp.dto;

import com.aivle.bookapp.domain.Book;

public class BookResponseDto {
    private Long id;
    private String title;
    private String author;
    private String publisher;
    private Integer price;

    // Entity(DB 데이터)를 DTO로 변환하는 생성자
    public BookResponseDto(Book book) {
        this.id = book.getId();
        this.title = book.getTitle();
        this.author = book.getAuthor();
        this.publisher = book.getPublisher();
        this.price = book.getPrice();
    }

    // --- Getter --- (응답용이므로 보통 Getter만 사용합니다)
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getPublisher() { return publisher; }
    public Integer getPrice() { return price; }
}