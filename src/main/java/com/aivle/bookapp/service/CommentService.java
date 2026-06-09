package com.aivle.bookapp.service;

import com.aivle.bookapp.domain.Comment;
import com.aivle.bookapp.domain.User;
import com.aivle.bookapp.dto.CommentCreateRequest;
import com.aivle.bookapp.dto.CommentResponse;
import com.aivle.bookapp.dto.CommentUpdateRequest;
import com.aivle.bookapp.repository.CommentRepository;
import jakarta.persistence.EntityManager;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final EntityManager entityManager;

    public CommentService(CommentRepository commentRepository, EntityManager entityManager) {
        this.commentRepository = commentRepository;
        this.entityManager = entityManager;
    }

    @Transactional
    public CommentResponse createComment(Long userId, CommentCreateRequest request) {
        /*
         * TODO: User 엔티티 완성 후 아래 블록 주석 해제 후 throw 제거
         *
         * User user = entityManager.find(User.class, userId);
         * if (user == null) {
         *     throw new ResponseStatusException(HttpStatus.NOT_FOUND,
         *         "사용자를 찾을 수 없습니다. id=" + userId);
         * }
         * Comment comment = new Comment(request.getBookId(), user, request.getContent());
         * return CommentResponse.from(commentRepository.save(comment));
         */
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
            "User 엔티티 완성 후 사용 가능합니다.");
    }

    @Transactional
    public CommentResponse updateComment(String commentId, Long userId, CommentUpdateRequest request) {
        Comment comment = findCommentOrThrow(commentId);
        checkOwnership(comment, userId);
        comment.updateContent(request.getContent());
        return CommentResponse.from(comment);
    }

    @Transactional
    public void deleteComment(String commentId, Long userId) {
        Comment comment = findCommentOrThrow(commentId);
        checkOwnership(comment, userId);
        commentRepository.delete(comment);
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> getCommentsByBookId(Long bookId) {
        return commentRepository.findAllByBookId(bookId).stream()
            .map(CommentResponse::from)
            .collect(Collectors.toList());
    }

    private Comment findCommentOrThrow(String commentId) {
        return commentRepository.findById(commentId)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "댓글을 찾을 수 없습니다. id=" + commentId));
    }

    private void checkOwnership(Comment comment, Long userId) {
        if (!comment.isOwnedBy(userId)) {
            throw new ResponseStatusException(
                HttpStatus.FORBIDDEN, "본인의 댓글만 수정/삭제할 수 있습니다.");
        }
    }
}
