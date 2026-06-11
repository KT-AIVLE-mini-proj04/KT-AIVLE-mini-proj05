package com.aivle.bookapp.repository;

import com.aivle.bookapp.domain.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    Page<Comment> findAllByBookId(Integer bookId, Pageable pageable);
}
