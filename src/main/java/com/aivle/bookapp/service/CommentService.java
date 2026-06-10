package com.aivle.bookapp.service;

import com.aivle.bookapp.domain.Comment;
import com.aivle.bookapp.dto.CommentCreateRequest;
import com.aivle.bookapp.dto.CommentResponse;
import com.aivle.bookapp.dto.CommentUpdateRequest;
import com.aivle.bookapp.repository.CommentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Transactional
    public CommentResponse createComment(Long userId, CommentCreateRequest request) {
        Comment comment = new Comment(request.getBookId(), userId, request.getContent());
        return CommentResponse.from(commentRepository.save(comment));
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
