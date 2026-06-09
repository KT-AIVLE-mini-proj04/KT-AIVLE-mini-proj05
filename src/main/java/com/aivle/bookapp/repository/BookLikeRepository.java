package com.aivle.bookapp.repository;

import com.aivle.bookapp.domain.Book;
import com.aivle.bookapp.domain.BookLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class BookLikeRepository extends JpaRepository<BookLike, Long> {

    // 특정 유저, 도서 좋아요 조회
    Optional<BookLike> findByUserAndBook(User user, Book book);

    // 좋아요 클릭 여부 확인
    boolean existsByUserAndBook(User user, Book book);

    long countByBook(Book book);
}
