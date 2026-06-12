package com.aivle.bookapp.service;

import com.aivle.bookapp.dto.BookCountResponseDto;
import com.aivle.bookapp.repository.BookLikeRepository;
import com.aivle.bookapp.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookLikeRepository bookLikeRepository;

    @InjectMocks
    private BookService bookService;

    @Test
    void getBookCountsReturnsAggregatedCounts() {
        given(bookRepository.count()).willReturn(12L);
        given(bookRepository.countBooksWithCover()).willReturn(7L);
        given(bookLikeRepository.countDistinctBookIds()).willReturn(5L);

        BookCountResponseDto response = bookService.getBookCounts();

        assertThat(response.getTotalBookCount()).isEqualTo(12L);
        assertThat(response.getCoverBookCount()).isEqualTo(7L);
        assertThat(response.getLikedBookCount()).isEqualTo(5L);
    }
}
