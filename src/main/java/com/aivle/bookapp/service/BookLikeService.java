package com.aivle.bookapp.service;

import com.aivle.bookapp.domain.Book;
import com.aivle.bookapp.domain.BookLike;
import com.aivle.bookapp.dto.BookLikeResponse;
import com.aivle.bookapp.repository.BookLikeRepository;
import com.aivle.bookapp.repository.BookRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookLikeService {

    private final BookLikeRepository bookLikeRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    @Transactional
    public BookLikeResponse toggleLike(Long bookId, Long userId) {
        // 1. 도서 및 유저 검증
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 도서입니다."));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        // 2. 좋아요 내역 조회
        Optional<BookLike> existingLike = bookLikeRepository.findByUserAndBook(user, book);

        boolean isLiked;

        // 3. 토글 로직
        if (existingLike.isPresent()) {
            // 이미 좋아요를 누른 상태라면 -> 좋아요 취소
            bookLikeRepository.delete(existingLike.get());
            book.decreaseLikeCount();
            isLiked = false;
        } else {
            // 좋아요를 누르지 않은 상태라면 -> 좋아요 추가
            BookLike newLike = new BookLike(user, book);
            bookLikeRepository.save(newLike);
            book.increaseLikeCount();
            isLiked = true;
        }

        // 4. 결과 반환
        return BookLikeResponse.of(isLiked, book.getLikeCount());
    }
}