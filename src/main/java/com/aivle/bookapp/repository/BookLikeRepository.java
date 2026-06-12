package com.aivle.bookapp.repository;

import com.aivle.bookapp.domain.Book;
import com.aivle.bookapp.domain.BookLike;
import com.aivle.bookapp.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookLikeRepository extends JpaRepository<BookLike, Integer> {

    // 특정 유저, 도서 좋아요 조회
    Optional<BookLike> findByBookAndUser(Book book, Users user); //User user,
    // 좋아요 클릭 여부 확인
//    boolean existsByUserAndBook(Book book); //User user,

    long countByBook(Book book);
}
