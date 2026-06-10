package com.aivle.bookapp.dto;

import com.aivle.bookapp.domain.Book;
import lombok.Getter;

@Getter
public class BookResponseDto {

    private Long id;
    private String coverImageUrl;

    public BookResponseDto(Book book) {
        this.id = book.getId();
        this.coverImageUrl = book.getCoverImageUrl();
    }
}
