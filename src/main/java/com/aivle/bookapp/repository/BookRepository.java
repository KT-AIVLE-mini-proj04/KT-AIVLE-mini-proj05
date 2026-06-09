package com.aivle.bookapp.repository;

import com.aivle.bookapp.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository // 이 인터페이스가 데이터베이스 접근자 역할을 함을 명시
public interface BookRepository extends JpaRepository<Book, Long> {
    // JpaRepository를 상속받기만 하면 
    // 기본 CRUD(save, findAll, findById, delete) 기능이 자동으로 생성됩니다!
}