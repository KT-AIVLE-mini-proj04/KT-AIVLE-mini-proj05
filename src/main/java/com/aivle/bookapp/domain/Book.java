package com.aivle.bookapp.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@Entity 
@Table(name = "book_info") // SQL의 book_info 테이블과 매칭
public class Book {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    @Column(name = "book_id") // SQL의 book_id (PK)와 매칭
    private Long id;

    // SQL의 users_id (FK)와 매칭. 당장 에러를 막기 위해 임시로 1번 유저로 세팅
    @Column(name = "users_id", nullable = false)
    private Long usersId = 1L;

    @Column(name = "title")
    private String title;       

    @Column(name = "author")
    private String author;      

    // SQL의 description 컬럼에 자바의 content 데이터를 넣음
    @Column(name = "description", columnDefinition = "TEXT") 
    private String content;

    // SQL의 cover 컬럼에 자바의 coverImageData 데이터를 넣음
    @Column(name = "cover", columnDefinition = "TEXT") 
    private String coverImageData;

    @CreationTimestamp 
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp 
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // 기본 생성자
    public Book() {}

    // 도서 등록용 생성자
    public Book(String title, String author, String content) {
        this.title = title;
        this.author = author;
        this.content = content;
    }

    // --- Getter 및 Setter ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUsersId() { return usersId; }
    public void setUsersId(Long usersId) { this.usersId = usersId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getCoverImageData() { return coverImageData; }
    public void setCoverImageData(String coverImageData) { this.coverImageData = coverImageData; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}