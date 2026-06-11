package com.aivle.bookapp.service;

import com.aivle.bookapp.domain.Comment;
import com.aivle.bookapp.dto.CommentCreateRequest;
import com.aivle.bookapp.dto.CommentResponse;
import com.aivle.bookapp.dto.CommentUpdateRequest;
import com.aivle.bookapp.repository.CommentRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Transactional
    public CommentResponse createComment(Integer usersId, CommentCreateRequest request) {
        Comment comment = new Comment(request.getBookId(), usersId, request.getContent());
        return CommentResponse.from(commentRepository.save(comment));
    }

    @Transactional
    public CommentResponse updateComment(Integer commentsId, Integer usersId, CommentUpdateRequest request) {
        Comment comment = findCommentOrThrow(commentsId);
        checkOwnership(comment, usersId);
        comment.updateContent(request.getContent());
        return CommentResponse.from(comment);
    }

    @Transactional
    public void deleteComment(Integer commentsId, Integer usersId) {
        Comment comment = findCommentOrThrow(commentsId);
        checkOwnership(comment, usersId);
        commentRepository.delete(comment);
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> getCommentsByBookId(Integer bookId, Integer page) {
        Pageable pageable = (page == null)
                ? Pageable.unpaged()
                : PageRequest.of(page, 10, Sort.by("createdAt").descending());
        return commentRepository.findAllByBookId(bookId, pageable)
                .map(CommentResponse::from)
                .getContent();
    }

    private Comment findCommentOrThrow(Integer commentsId) {
        return commentRepository.findById(commentsId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "댓글을 찾을 수 없습니다. id=" + commentsId));
    }

    private void checkOwnership(Comment comment, Integer usersId) {
        if (!comment.isOwnedBy(usersId)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "본인의 댓글만 수정/삭제할 수 있습니다.");
        }
    }
}
