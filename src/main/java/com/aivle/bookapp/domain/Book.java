package com.aivle.bookapp.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@Entity // 이 클래스가 데이터베이스의 테이블과 매칭됨을 알려주는 핵심 어노테이션
public class Book {

    @Id // 이 필드가 고유키(Primary Key)임을 나타냄
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID 값을 DB가 자동으로 1씩 증가시켜줌
    private Long id;

    private String title;       // 책 제목
    private String author;      // 저자

    // 1. 본문(내용)
    @Column(columnDefinition = "TEXT") // 본문은 글자가 길어질 수 있으므로 TEXT 타입 지정
    private String content;

    // 2. AI 생성 표지 데이터 (Data URL 방식 저장)
    @Column(columnDefinition = "TEXT") // 이미지 데이터 문자열이 매우 길기 때문에 TEXT로 지정
    private String coverImageData;

    // 3. 등록일 / 수정일
    @CreationTimestamp // 데이터가 처음 저장될 때 시간 자동 기록
    private LocalDateTime createdAt;

    @UpdateTimestamp // 데이터가 수정될 때마다 시간 자동 갱신
    private LocalDateTime updatedAt;

    // 기본 생성자 (JPA 사용 시 필수)
    public Book() {
    }

    // 4. 데이터 세팅을 위한 생성자 수정 (content 추가, 표지/시간은 나중에 세팅되므로 제외)
    public Book(String title, String author, String content) {
        this.title = title;
        this.author = author;
        this.content = content;
    }

    // --- Getter 및 Setter ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getCoverImageData() { return coverImageData; }
    public void setCoverImageData(String coverImageData) { this.coverImageData = coverImageData; }

    // (시간은 보통 변경할 일이 없어서 Getter만 둡니다)
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}