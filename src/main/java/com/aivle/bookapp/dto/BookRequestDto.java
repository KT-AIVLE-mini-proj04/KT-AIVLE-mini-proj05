package com.aivle.bookapp.dto;

public class BookRequestDto {
    private String title;
    private String author;

    // 기본 생성자
    public BookRequestDto() {}

    // --- Getter 및 Setter ---
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
}