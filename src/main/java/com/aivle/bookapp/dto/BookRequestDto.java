package com.aivle.bookapp.dto;

public class BookRequestDto {
    private String title;
    private String author;
    private String publisher;
    private Integer price;

    // 기본 생성자
    public BookRequestDto() {}

    // --- Getter 및 Setter ---
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getPublisher() { return publisher; }
    public void setPublisher(String publisher) { this.publisher = publisher; }

    public Integer getPrice() { return price; }
    public void setPrice(Integer price) { this.price = price; }
}