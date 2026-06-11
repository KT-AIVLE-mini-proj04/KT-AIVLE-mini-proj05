package com.aivle.bookapp.service;

import com.aivle.bookapp.domain.Comment;
import com.aivle.bookapp.domain.Users;
import com.aivle.bookapp.dto.CommentCreateRequest;
import com.aivle.bookapp.dto.CommentResponse;
import com.aivle.bookapp.dto.CommentUpdateRequest;
import com.aivle.bookapp.global.util.JwtTokenProvider;
import com.aivle.bookapp.repository.CommentRepository;
import com.aivle.bookapp.repository.UserRepository;
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

    private static final String BEARER_PREFIX = "Bearer ";

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public CommentService(CommentRepository commentRepository,
                          UserRepository userRepository,
                          JwtTokenProvider jwtTokenProvider) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Transactional
    public CommentResponse createComment(String authorizationHeader, CommentCreateRequest request) {
        Users user = resolveUser(authorizationHeader);
        Comment comment = new Comment(request.getBookId(), user.getUsersId(), request.getContent());
        Comment saved = commentRepository.save(comment);
        return CommentResponse.from(saved, user);
    }

    @Transactional
    public CommentResponse updateComment(Integer commentsId, String authorizationHeader, CommentUpdateRequest request) {
        Users user = resolveUser(authorizationHeader);
        Comment comment = findCommentOrThrow(commentsId);
        checkOwnership(comment, user.getUsersId());
        comment.updateContent(request.getContent());
        return CommentResponse.from(comment, user);
    }

    @Transactional
    public void deleteComment(Integer commentsId, String authorizationHeader) {
        Users user = resolveUser(authorizationHeader);
        Comment comment = findCommentOrThrow(commentsId);
        checkOwnership(comment, user.getUsersId());
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

    private Users resolveUser(String authorizationHeader) {
        String token = extractToken(authorizationHeader);
        if (!jwtTokenProvider.validateToken(token)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다.");
        }
        String loginId = jwtTokenProvider.getEmailFromToken(token);
        return userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "사용자를 찾을 수 없습니다."));
    }

    private String extractToken(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith(BEARER_PREFIX)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization 헤더가 필요합니다.");
        }
        return authorizationHeader.substring(BEARER_PREFIX.length());
    }
}
