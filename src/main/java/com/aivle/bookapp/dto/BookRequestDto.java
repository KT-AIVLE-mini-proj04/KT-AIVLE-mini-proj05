package com.aivle.bookapp.dto;

// 유효성 검사(필수 값, 길이 제한)를 위한 라이브러리 추가
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class BookRequestDto {
    
    // 기획서 요구사항: 필수 입력 + 길이 제한 유효성 검사 적용
    @NotBlank(message = "도서 제목은 필수 입력 항목입니다.")
    @Size(max = 100, message = "제목은 100자를 초과할 수 없습니다.")
    private String title;

    // 기획서 요구사항: 필수 입력 검사 적용
    @NotBlank(message = "저자는 필수 입력 항목입니다.")
    private String author;

    // 1. 기획서 누락 필드 추가: 본문(내용) 및 필수 입력 검사 적용
    @NotBlank(message = "본문 내용은 필수 입력 항목입니다.")
    private String description;

    // 기본 생성자
    public BookRequestDto() {}

    // --- Getter 및 Setter ---
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}