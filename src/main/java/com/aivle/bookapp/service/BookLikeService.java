package com.aivle.bookapp.service;

import com.aivle.bookapp.domain.Book;
import com.aivle.bookapp.domain.BookLike;
import com.aivle.bookapp.dto.BookLikeResponse;
import com.aivle.bookapp.repository.BookLikeRepository;
import com.aivle.bookapp.repository.BookRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookLikeService {

    private final BookLikeRepository bookLikeRepository;
    private final BookRepository bookRepository;
//    private final UserRepository userRepository;

    /*
    게시물 좋아요
     */
    @Transactional
    public BookLikeResponse toggleLike(Long bookId) { //, Long userId
        // 1. 도서 및 유저 검증
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 도서입니다."));
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        // 2. 좋아요 내역 조회
        Optional<BookLike> existingLike = bookLikeRepository.findByBook(book);

        boolean isLiked;

        // 3. 로직
        if (existingLike.isPresent()) {
            // 이미 존재하면 삭제 (좋아요 취소)
            bookLikeRepository.delete(existingLike.get());
            isLiked = false;
        } else {
            // 존재하지 않으면 저장 (좋아요 추가)
            bookLikeRepository.save(new BookLike(book)); //user
            isLiked = true;
        }

        long totalLikes = bookLikeRepository.countByBook(book);

        // 4. 결과 반환
        return BookLikeResponse.of(isLiked, (int) totalLikes);
    }
}