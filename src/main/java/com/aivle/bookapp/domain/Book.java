package com.aivle.bookapp.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;
import java.time.LocalDateTime;

@Entity 
@Table(name = "book_info")
public class Book {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    @Column(name = "book_id")
    @JdbcTypeCode(SqlTypes.INTEGER)
    private Long bookId;

    @Column(name = "user_id", nullable = false)
    @JdbcTypeCode(SqlTypes.INTEGER)
    private Long userId;

    @Column(name = "title")
    private String title;       

    @Column(name = "author")
    private String author;      

    @Column(name = "description", columnDefinition = "TEXT") 
    private String description;

    @Column(name = "cover", columnDefinition = "TEXT") 
    private String cover;

    @CreationTimestamp 
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp 
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // 기본 생성자
    public Book() {}

    // 도서 등록용 생성자
    public Book(String title, String author, String description, Long userId) {
        this.title = title;
        this.author = author;
        this.description = description;
        this.userId = userId;
    }

    // --- Getter 및 Setter ---
    public Long getBookId() { return bookId; }
    public void setBookId(Long bookId) { this.bookId = bookId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCover() { return cover; }
    public void setCover(String cover) { this.cover = cover; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}