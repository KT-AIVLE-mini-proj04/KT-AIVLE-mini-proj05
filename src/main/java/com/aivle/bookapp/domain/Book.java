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

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "book_info")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    @JdbcTypeCode(SqlTypes.INTEGER)
    private Long bookId;

    @Column(name = "users_id", nullable = false)
    @JdbcTypeCode(SqlTypes.INTEGER)
    private Long usersId;

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
//    private String title;

    //    private int likeCount = 0;
//
//    public void increaseLikeCount() {
//        this.likeCount++;
//    }
//
//    public void decreaseLikeCount() {
//        if (this.likeCount > 0) {
//            this.likeCount--;
//        }
//    }
    // 기본 생성자
    public Book() {}

    // 도서 등록용 생성자
    public Book(String title, String author, String description, Long usersId) {
        this.title = title;
        this.author = author;
        this.description = description;
        this.usersId = usersId;
    }

}
