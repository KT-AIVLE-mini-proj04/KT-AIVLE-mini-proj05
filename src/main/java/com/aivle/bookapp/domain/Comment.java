package com.aivle.bookapp.domain;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@EntityListeners(AuditingEntityListener.class)
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comments_id")
    private Integer commentsId;

    @Column(name = "book_id", nullable = false)
    private Integer bookId;

    @Column(name = "users_id", nullable = false)
    private Integer usersId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id", insertable = false, updatable = false)
    private Users author;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    protected Comment() {}

    public Comment(Integer bookId, Integer usersId, String content) {
        this.bookId = bookId;
        this.usersId = usersId;
        this.content = content;
    }

    public Integer getCommentsId() { return commentsId; }
    public Integer getBookId() { return bookId; }
    public Integer getUsersId() { return usersId; }
    public Users getAuthor() { return author; }
    public String getContent() { return content; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    public void updateContent(String content) {
        this.content = content;
    }

    public boolean isOwnedBy(Integer requestUsersId) {
        return this.usersId != null && this.usersId.equals(requestUsersId);
    }
}
