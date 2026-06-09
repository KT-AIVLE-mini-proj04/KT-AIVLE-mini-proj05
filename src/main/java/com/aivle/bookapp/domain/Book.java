package com.aivle.bookapp.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity // 이 클래스가 데이터베이스의 테이블과 매칭됨을 알려주는 핵심 어노테이션
public class Book {

    @Id // 이 필드가 고유키(Primary Key)임을 나타냄
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID 값을 DB가 자동으로 1씩 증가시켜줌
    private Long id;

    private String title;       // 책 제목
    private String author;      // 저자
    private String publisher;   // 출판사
    private Integer price;      // 가격

    // 기본 생성자 (JPA 사용 시 필수)
    public Book() {
    }

    // 데이터 세팅을 위한 생성자
    public Book(String title, String author, String publisher, Integer price) {
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.price = price;
    }

    // --- Getter 및 Setter ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getPublisher() { return publisher; }
    public void setPublisher(String publisher) { this.publisher = publisher; }

    public Integer getPrice() { return price; }
    public void setPrice(Integer price) { this.price = price; }
}