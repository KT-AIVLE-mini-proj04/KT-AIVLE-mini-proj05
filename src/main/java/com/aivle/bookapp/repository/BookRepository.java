package com.aivle.bookapp.repository;

import com.aivle.bookapp.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    // 검색 기능용: 제목에 특정 단어(keyword)가 포함된 책들을 찾아오는 기능
    List<Book> findByTitleContaining(String keyword);

    @Query("select count(b) from Book b where b.cover is not null and trim(b.cover) <> ''")
    long countBooksWithCover();

    // JpaRepository를 상속받기만 하면
    // 기본 CRUD(save, findAll, findById, delete) 기능이 자동으로 생성됩니다!
}
